package com.biel.qmsgather.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624ManualConfig;
import com.biel.qmsgather.mapper.DfExcelC98bWireframe20240624ManualConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bWireframe20240624ManualConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_20240624_manual_config】的数据库操作Service实现
* @createDate 2025-02-20 15:34:45
*/
@Service
@Slf4j
public class DfExcelC98bWireframe20240624ManualConfigServiceImpl extends ServiceImpl<DfExcelC98bWireframe20240624ManualConfigMapper, DfExcelC98bWireframe20240624ManualConfig>
    implements DfExcelC98bWireframe20240624ManualConfigService {



    @Autowired
    private DfExcelC98bWireframe20240624ManualConfigMapper configMapper;

    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        ZipSecureFile.setMinInflateRatio(0.001);
        int successCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            log.info("开始读取Excel配置数据，处理第4-8行");

            // 读取第4行到第8行
            for (int rowIndex = 3; rowIndex <= 7; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                try {
                    DfExcelC98bWireframe20240624ManualConfig config = new DfExcelC98bWireframe20240624ManualConfig();

                    // 遍历单元格并设置对应的属性
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = getCellValue(cell, evaluator, formatter);
                        setCellValueToConfig(config, cellIndex, cellValue,batchId);
                    }

                    if (isValidConfigData(config)) {
                        configMapper.insert(config);
                        successCount++;
                        logConfigData(config, rowIndex);
                    } else {
                        log.warn("第{}行配置数据不完整，跳过", rowIndex + 1);
                    }

                } catch (Exception e) {
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
            }

            log.info("配置数据导入完成，成功导入{}条记录", successCount);

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return successCount;
    }

    private void setCellValueToConfig(DfExcelC98bWireframe20240624ManualConfig config, int cellIndex, String cellValue,String batchId) {
        config.setBatchId(batchId);
        switch (cellIndex) {

            case 0: config.setCol1(cellValue); break;  // 记录时间
            case 1: config.setCol2(cellValue); break;  // 外形宽 - 95
            case 2: config.setCol3(cellValue); break;  // 外形宽 - 96
            case 3: config.setCol4(cellValue); break;  // 凹槽宽 - 97
            case 4: config.setCol5(cellValue); break;  // 凹槽宽 - 98
            case 5: config.setCol6(cellValue); break;  // 凹槽宽 - 99
            case 6: config.setCol7(cellValue); break;  // 凹槽宽 - 100
            case 7: config.setCol8(cellValue); break;  // 凹槽宽 - 101
            case 8: config.setCol9(cellValue); break;  // 凹槽长 - 102
            case 9: config.setCol10(cellValue); break; // 全身油墨 - 103
            case 10: config.setCol11(cellValue); break; // 长差值
            case 11: config.setCol12(cellValue); break; // 宽差值
            case 12: config.setCol13(cellValue); break; // 凹槽宽差
            case 13: config.setCol14(cellValue); break;
            case 14: config.setCol15(cellValue); break;
            case 15: config.setCol16(cellValue); break;
            case 16: config.setCol17(cellValue); break;
            case 17: config.setCol18(cellValue); break;
            case 18: config.setCol19(cellValue); break;
            case 19: config.setCol20(cellValue); break;
            case 20: config.setCol21(cellValue); break;
            case 21: config.setCol22(cellValue); break;
            case 22: config.setCol23(cellValue); break;
            case 23: config.setCol24(cellValue); break;
            case 24: config.setCol25(cellValue); break;
            case 25: config.setCol26(cellValue); break;
            case 26: config.setCol27(cellValue); break;
            case 27: config.setCol28(cellValue); break;
            case 28: config.setCol29(cellValue); break;
            case 29: config.setCol30(cellValue); break;
            case 30: config.setCol31(cellValue); break;
            case 31: config.setCol32(cellValue); break;
            case 32: config.setCol33(cellValue); break;
            case 33: config.setCol34(cellValue); break;

        }
    }

    private void logConfigData(DfExcelC98bWireframe20240624ManualConfig config, int rowIndex) {
        log.info("==================== 第 {} 行数据 ====================", rowIndex + 1);
        log.info("记录时间: {}", config.getCol1());
        log.info("外形宽-95: {}", config.getCol2());
        log.info("外形宽-96: {}", config.getCol3());
        log.info("凹槽宽-97: {}", config.getCol4());
        log.info("凹槽宽-98: {}", config.getCol5());
        log.info("凹槽宽-99: {}", config.getCol6());
        log.info("凹槽宽-100: {}", config.getCol7());
        log.info("凹槽宽-101: {}", config.getCol8());
        log.info("凹槽长-102: {}", config.getCol9());
        log.info("全身油墨-103: {}", config.getCol10());
        log.info("长差值: {}", config.getCol11());
        log.info("宽差值: {}", config.getCol12());
        log.info("凹槽宽差: {}", config.getCol13());
        // ... 其他字段的日志记录
        log.info("================================================");
    }

    private boolean isValidConfigData(DfExcelC98bWireframe20240624ManualConfig config) {
        return config.getCol1() != null && !config.getCol1().isEmpty()
                && config.getCol2() != null
                && config.getCol3() != null
                && config.getCol4() != null
                && config.getCol5() != null
                && config.getCol6() != null
                && config.getCol7() != null
                && config.getCol8() != null
                && config.getCol9() != null
                && config.getCol10() != null;
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) return "";
        try {
            switch (cell.getCellType()) {
                case FORMULA:
                    return getFormulaValue(cell, evaluator, formatter);
                case NUMERIC:
                    return getNumericValue(cell, formatter);
                case STRING:
                    return cell.getStringCellValue().trim();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case ERROR:
                    return "ERROR: " + cell.getErrorCellValue();
                case BLANK:
                default:
                    return "";
            }
        } catch (Exception e) {
            log.warn("处理单元格值时发生错误: {}", e.getMessage());
            return "";
        }
    }

    private String getFormulaValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        try {
            CellValue cellValue = evaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return sdf.format(cell.getDateCellValue());
                    }
                    return formatter.formatCellValue(cell, evaluator);
                case STRING:
                    return cellValue.getStringValue();
                case BOOLEAN:
                    return String.valueOf(cellValue.getBooleanValue());
                default:
                    return formatter.formatCellValue(cell, evaluator);
            }
        } catch (Exception e) {
            return cell.getCellFormula();
        }
    }

    private String getNumericValue(Cell cell, DataFormatter formatter) {
        if (DateUtil.isCellDateFormatted(cell)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(cell.getDateCellValue());
        }
        return formatter.formatCellValue(cell);
    }
}









