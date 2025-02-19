package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm2Config;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm2ConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm2ConfigService;
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
* @description 针对表【df_excel_c98b_ink_bm2_config】的数据库操作Service实现
* @createDate 2025-02-18 11:57:41
*/
@Slf4j
@Service
public class DfExcelC98bInkBm2ConfigServiceImpl extends ServiceImpl<DfExcelC98bInkBm2ConfigMapper, DfExcelC98bInkBm2Config>
    implements DfExcelC98bInkBm2ConfigService{


    @Autowired
    private DfExcelC98bInkBm2ConfigMapper configMapper;


    public int importBm2ConfigFromExcel(MultipartFile file) {
        List<DfExcelC98bInkBm2Config> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            try {
                Sheet sheet = workbook.getSheet("BM2");
                if (sheet == null) {
                    log.error("未找到工作表: BM2");
                    return 0;
                }

                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                DataFormatter formatter = new DataFormatter();
                log.info("开始读取Excel配置数据，处理第1-6行");

                // 遍历指定范围的行
                for (int rowIndex = 0; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) {
                        log.warn("第 {} 行为空，跳过...", rowIndex + 1);
                        continue;
                    }

                    DfExcelC98bInkBm2Config config = new DfExcelC98bInkBm2Config();

                    try {
                        // 获取并设置数据
                        config.setCol1(getMergedCellValue(sheet, rowIndex, 0, row.getCell(0), evaluator, formatter));
                        config.setCol2(getMergedCellValue(sheet, rowIndex, 1, row.getCell(1), evaluator, formatter));
                        config.setCol3(getMergedCellValue(sheet, rowIndex, 2, row.getCell(2), evaluator, formatter));
                        config.setCol4(getMergedCellValue(sheet, rowIndex, 3, row.getCell(3), evaluator, formatter));
                        config.setCol5(getMergedCellValue(sheet, rowIndex, 4, row.getCell(4), evaluator, formatter));

                        if (isValidBm2ConfigData(config)) {
                            dataList.add(config);
                            log.debug("成功读取第{}行配置数据: {}", rowIndex + 1, config);
                        } else {
                            log.warn("第{}行配置数据不完整，跳过: {}", rowIndex + 1, config);
                        }

                    } catch (Exception e) {
                        log.error("读取第{}行配置数据失败: {}", rowIndex + 1, e.getMessage());
                    }
                }

            } finally {
                workbook.close();
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveBm2Config(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveBm2Config(List<DfExcelC98bInkBm2Config> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkBm2Config data : dataList) {
            try {
                configMapper.insert(data);
                successCount++;
                log.info("==================== 保存配置数据 ====================");
                log.info("列1: {}", data.getCol1());
                log.info("列2: {}", data.getCol2());
                log.info("列3: {}", data.getCol3());
                log.info("列4: {}", data.getCol4());
                log.info("列5: {}", data.getCol5());
                log.info("================================================");
            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", data.getCol1(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidBm2ConfigData(DfExcelC98bInkBm2Config data) {
        return data.getCol1() != null && !data.getCol1().isEmpty()
                && data.getCol2() != null && !data.getCol2().isEmpty()
                && data.getCol3() != null && !data.getCol3().isEmpty()
                && data.getCol4() != null && !data.getCol4().isEmpty()
                && data.getCol5() != null && !data.getCol5().isEmpty();
    }



    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
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
    }








}




