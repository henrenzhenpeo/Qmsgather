package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm1;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm1Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm1Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm1】的数据库操作Service实现
* @createDate 2025-02-18 11:57:31
*/
@Service
@Slf4j
public class DfExcelC98bInkBm1ServiceImpl extends ServiceImpl<DfExcelC98bInkBm1Mapper, DfExcelC98bInkBm1>
    implements DfExcelC98bInkBm1Service{



    @Autowired
    private DfExcelC98bInkBm1Mapper dfExcelC98bInkBm1Mapper;



    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        List<DfExcelC98bInkBm1> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        ZipSecureFile.setMinInflateRatio(0.001);
        String sheetName = "BM1";
        int startRow = 6;
        int consecutiveNullCount = 0;

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                log.error("未找到工作表: {}", sheetName);
                result.put("success", false);
                result.put("message", "未找到指定的工作表: " + sheetName);
                return result;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            for (int i = startRow; ; i++) {
                Row row = sheet.getRow(i);

                if (row == null || isFirstColumnEmpty(row)) {
                    consecutiveNullCount++;
                    if (consecutiveNullCount >= 3) {
                        break;
                    }
                    continue;
                }

                consecutiveNullCount = 0;

                try {
                    DfExcelC98bInkBm1 entity = new DfExcelC98bInkBm1();

                    // 设置时间
                    String time = getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter);
                    entity.setTime(time);


                    entity.setBatchId(batchId);
                    // 设置J3-J17的值
                    entity.setJ3(new BigDecimal(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter)));
                    entity.setJ8(new BigDecimal(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter)));
                    entity.setJ13(new BigDecimal(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter)));
                    entity.setJ17(new BigDecimal(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter)));

                    // 设置平均值
                    String avgStr = getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter);
                    entity.setAverage(new BigDecimal(avgStr));

                    // 设置结果
                    entity.setResult(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));

                    // 设置默认值
                    entity.setMachineNumber(1);
                    entity.setStatus("正常");
                    entity.setNotes("");

                    if (isValidData(entity)) {
                        dataList.add(entity);
                        successCount++;
                        log.debug("成功读取第{}行数据: {}", i + 1, entity);
                    } else {
                        failCount++;
                        log.warn("第{}行数据不完整，跳过: {}", i + 1, entity);
                    }

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                successCount = batchSave(dataList);
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "Excel文件读取失败：" + e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkBm1> dataList) {
        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkBm1 data : dataList) {
            try {
                dfExcelC98bInkBm1Mapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkBm1 data) {
        return data.getTime() != null && !data.getTime().isEmpty()
                && data.getJ3() != null
                && data.getJ8() != null
                && data.getJ13() != null
                && data.getJ17() != null
                && data.getAverage() != null;
    }

    // 其他辅助方法保持不变
    private boolean isFirstColumnEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellType() == CellType.BLANK;
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row, col)) {
                Cell firstCell = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
                return getCellValue(firstCell, evaluator, formatter);
            }
        }
        return getCellValue(cell, evaluator, formatter);
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) {
            return "null";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return formatter.formatCellValue(cell);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return formatter.formatCellValue(cell, evaluator);
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return "";
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return "unknown";
        }
    }



}




