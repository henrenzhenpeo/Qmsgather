package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm1Config01;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm1Config01Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm1Config01Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm1_config_01】的数据库操作Service实现
* @createDate 2025-02-21 10:02:51
*/
@Service
@Slf4j
public class DfExcelC98bInkBm1Config01ServiceImpl extends ServiceImpl<DfExcelC98bInkBm1Config01Mapper, DfExcelC98bInkBm1Config01>
    implements DfExcelC98bInkBm1Config01Service{





    @Autowired
    private DfExcelC98bInkBm1Config01Mapper mapper;




    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        log.info("开始读取Excel文件...");
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            try {
                Sheet sheet = workbook.getSheet("BM1");
                if (sheet == null) {
                    log.error("工作表'BM1'未找到");
                    return 0;
                }

                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                DataFormatter formatter = new DataFormatter();

                // 创建实体对象
                DfExcelC98bInkBm1Config01 config = new DfExcelC98bInkBm1Config01();
                config.setBatchId(batchId);

                // 读取第二行数据
                Row row2 = sheet.getRow(1);
                if (row2 != null) {
                    config.setModelCode(getMergedCellValue(sheet, 1, 0, row2.getCell(0), evaluator, formatter));
                    config.setMeasuringInstrument(getMergedCellValue(sheet, 1, 1, row2.getCell(1), evaluator, formatter));
                    log.info("型号: {}", config.getModelCode());
                    log.info("测量仪器: {}", config.getMeasuringInstrument());
                }

                // 读取第四行数据
                Row row4 = sheet.getRow(3);
                if (row4 != null) {
                    config.setNominalValue(getMergedCellValue(sheet, 3, 0, row4.getCell(0), evaluator, formatter));
                    config.setToleranceMax(getMergedCellValue(sheet, 3, 1, row4.getCell(1), evaluator, formatter));
                    config.setToleranceMin(getMergedCellValue(sheet, 3, 2, row4.getCell(2), evaluator, formatter));
                    config.setUpperLimit(getMergedCellValue(sheet, 3, 3, row4.getCell(3), evaluator, formatter));
                    config.setLowerLimit(getMergedCellValue(sheet, 3, 4, row4.getCell(4), evaluator, formatter));

                    log.info("读取到的配置信息: {}", config);
                }

                // 数据验证和保存
                if (isValidConfig(config)) {
                    int result = mapper.insert(config);
                    log.info("数据保存结果: {}", result > 0 ? "成功" : "失败");
                    return result;
                } else {
                    log.warn("配置数据不完整: {}", config);
                    return 0;
                }

            } finally {
                workbook.close();
            }

        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel文件读取失败", e);
        }
    }

    private boolean isValidConfig(DfExcelC98bInkBm1Config01 config) {
        return config.getModelCode() != null && !config.getModelCode().isEmpty()
                && config.getMeasuringInstrument() != null && !config.getMeasuringInstrument().isEmpty()
                && config.getNominalValue() != null
                && config.getToleranceMax() != null
                && config.getToleranceMin() != null
                && config.getUpperLimit() != null
                && config.getLowerLimit() != null;
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
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return formatter.formatCellValue(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return formatter.formatCellValue(cell, evaluator);
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return null;
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return null;
        }
    }


}




