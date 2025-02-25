package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfYfExcelSandblastingUploadTemplateData;
import com.biel.qmsgather.mapper.DfYfExcelSandblastingUploadTemplateDataMapper;
import com.biel.qmsgather.service.DfYfExcelSandblastingUploadTemplateDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_yf_excel_sandblasting_upload_template_data(水质数据表)】的数据库操作Service实现
* @createDate 2025-02-25 09:43:47
*/
@Service
@Slf4j
public class DfYfExcelSandblastingUploadTemplateDataServiceImpl extends ServiceImpl<DfYfExcelSandblastingUploadTemplateDataMapper, DfYfExcelSandblastingUploadTemplateData>
    implements DfYfExcelSandblastingUploadTemplateDataService{



    @Autowired
    private DfYfExcelSandblastingUploadTemplateDataService dfYfExcelSandblastingUploadTemplateDataService;

    private static final int BATCH_SIZE = 50;

    public int importDataFromExcel(MultipartFile file) {
        log.info("开始读取Excel文件...");
        List<DfYfExcelSandblastingUploadTemplateData> dataList = new ArrayList<>();
        int successCount = 0;

        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("喷砂");

            if (sheet == null) {
                log.error("未找到工作表: 喷砂");
                return 0;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 从第5行开始读取数据
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isEmptyRow(row)) {
                    continue;
                }

                try {
                    DfYfExcelSandblastingUploadTemplateData data = new DfYfExcelSandblastingUploadTemplateData();

                    // 映射数据到实体类
                    data.setTestTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setClarity(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    data.setGranularity(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    data.setLuminosity(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    data.setTester(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));

                    dataList.add(data);

                    // 批量处理
                    if (dataList.size() >= BATCH_SIZE) {
                        successCount += batchSaveData(dataList);
                        dataList.clear();
                    }

                } catch (Exception e) {
                    log.error("处理第{}行数据时出错: {}", i + 1, e.getMessage());
                }
            }

            // 处理剩余数据
            if (!dataList.isEmpty()) {
                successCount += batchSaveData(dataList);
            }

            workbook.close();

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        log.info("Excel文件读取完成，成功导入 {} 条记录", successCount);
        return successCount;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveData(List<DfYfExcelSandblastingUploadTemplateData> dataList) {
        try {
            dfYfExcelSandblastingUploadTemplateDataService.saveBatch(dataList);
            log.info("成功批量保存{}条数据", dataList.size());
            return dataList.size();
        } catch (Exception e) {
            log.error("批量保存数据失败: {}", e.getMessage());
            throw new RuntimeException("批量保存数据失败", e);
        }
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = cell.toString().trim();
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取合并单元格的值
     */
    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        // 检查是否是合并单元格
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row, col)) {
                // 获取合并区域的第一个单元格的值
                Cell firstCell = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
                return getCellValue(firstCell, evaluator, formatter);
            }
        }
        return getCellValue(cell, evaluator, formatter);
    }

    /**
     * 获取单元格的值
     */
    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) {
            return "null";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    } else {
                        // 使用DataFormatter来保持Excel中的格式
                        return formatter.formatCellValue(cell);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        // 使用FormulaEvaluator计算公式结果
                        return formatter.formatCellValue(cell, evaluator);
                    } catch (Exception e) {
                        // 如果公式计算失败，则返回公式字符串
                        return cell.getCellFormula();
                    }
                case BLANK:
                    return "";
                case ERROR:
                    return "ERROR: " + cell.getErrorCellValue();
                default:
                    return "unknown";
            }
        } catch (Exception e) {
            log.error("获取单元格值时出错: {}", e.getMessage());
            return "ERROR";
        }
    }


}




