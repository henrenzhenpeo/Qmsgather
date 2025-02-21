package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwireandoilConfig01;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm3andwireandoilConfig01Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3andwireandoilConfig01Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andwireandoil_config_01】的数据库操作Service实现
* @createDate 2025-02-21 10:03:10
*/
@Slf4j
@Service
public class DfExcelC98bInkBm3andwireandoilConfig01ServiceImpl extends ServiceImpl<DfExcelC98bInkBm3andwireandoilConfig01Mapper, DfExcelC98bInkBm3andwireandoilConfig01>
    implements DfExcelC98bInkBm3andwireandoilConfig01Service{



    @Autowired
    private DfExcelC98bInkBm3andwireandoilConfig01Mapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        log.info("开始读取Excel配置文件...");
        ZipSecureFile.setMinInflateRatio(0.001);

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("BM1+BM2+线框+光油");

            if (sheet == null) {
                log.error("未找到指定工作表");
                return 0;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 创建配置对象
            DfExcelC98bInkBm3andwireandoilConfig01 config = new DfExcelC98bInkBm3andwireandoilConfig01();

            config.setBatchId(batchId);

            // 读取第二行数据
            Row row2 = sheet.getRow(1);
            if (row2 != null) {
                config.setModelCode(getMergedCellValue(sheet, 1, 0, row2.getCell(0), evaluator, formatter));
                config.setMeasuringInstrument(getMergedCellValue(sheet, 1, 1, row2.getCell(1), evaluator, formatter));
                log.debug("读取型号: {}, 测量仪器: {}", config.getModelCode(), config.getMeasuringInstrument());
            }

            // 读取第三行数据
            Row row3 = sheet.getRow(2);
            if (row3 != null) {
                config.setNominalValue(getMergedCellValue(sheet, 2, 2, row3.getCell(2), evaluator, formatter));
                config.setToleranceMax(getMergedCellValue(sheet, 2, 3, row3.getCell(3), evaluator, formatter));
                config.setToleranceMin(getMergedCellValue(sheet, 2, 4, row3.getCell(4), evaluator, formatter));
                config.setUpperLimit(getMergedCellValue(sheet, 2, 5, row3.getCell(5), evaluator, formatter));
                config.setLowerLimit(getMergedCellValue(sheet, 2, 6, row3.getCell(6), evaluator, formatter));
                config.setAllUpperLimit(getMergedCellValue(sheet, 2, 7, row3.getCell(7), evaluator, formatter));
                config.setAllLowerLimit(getMergedCellValue(sheet, 2, 9, row3.getCell(9), evaluator, formatter));
            }

            if (isValidConfigData(config)) {
                try {
                    mapper.insert(config);
                    log.info("配置数据保存成功: {}", config);
                    return 1;
                } catch (Exception e) {
                    log.error("保存配置数据失败: {}", e.getMessage());
                    throw new RuntimeException("保存配置数据失败", e);
                }
            } else {
                log.warn("配置数据不完整: {}", config);
                return 0;
            }

        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }
    }

    private boolean isValidConfigData(DfExcelC98bInkBm3andwireandoilConfig01 config) {
        return config.getModelCode() != null && !config.getModelCode().isEmpty()
                && config.getMeasuringInstrument() != null && !config.getMeasuringInstrument().isEmpty()
                && config.getNominalValue() != null
                && config.getToleranceMax() != null
                && config.getToleranceMin() != null
                && config.getUpperLimit() != null
                && config.getLowerLimit() != null
                && config.getAllUpperLimit() != null
                && config.getAllLowerLimit() != null;
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




