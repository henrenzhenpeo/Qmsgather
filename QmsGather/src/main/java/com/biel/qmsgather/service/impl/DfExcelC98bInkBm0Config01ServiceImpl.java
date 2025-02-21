package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm0Config01;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm0Config01Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm0Config01Service;
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
* @description 针对表【df_excel_c98b_ink_bm0_config_01】的数据库操作Service实现
* @createDate 2025-02-21 10:02:46
*/
@Service
@Slf4j
public class DfExcelC98bInkBm0Config01ServiceImpl extends ServiceImpl<DfExcelC98bInkBm0Config01Mapper, DfExcelC98bInkBm0Config01>
    implements DfExcelC98bInkBm0Config01Service{


    @Autowired
    private DfExcelC98bInkBm0Config01Mapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        log.info("开始读取Excel配置文件...");
        ZipSecureFile.setMinInflateRatio(0.001);

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("BM0");

            if (sheet == null) {
                log.error("未找到BM0工作表");
                throw new RuntimeException("Excel文件格式错误:未找到BM0工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 创建配置对象
            DfExcelC98bInkBm0Config01 config = new DfExcelC98bInkBm0Config01();

            config.setBatchId(batchId);

            // 读取型号(第2行第1列)
            Row row1 = sheet.getRow(1);
            if (row1 != null) {
                Cell cell = row1.getCell(0);
                String modelCode = getMergedCellValue(sheet, 1, 0, cell, evaluator, formatter);
                config.setModelCode(modelCode);
                log.debug("读取型号: {}", modelCode);
            }

            // 读取第3行配置数据
            Row row2 = sheet.getRow(2);
            if (row2 != null) {
                config.setMeasuringInstrument(getMergedCellValue(sheet, 2, 0, row2.getCell(0), evaluator, formatter));
                config.setNominalValue(getMergedCellValue(sheet, 2, 1, row2.getCell(1), evaluator, formatter));
                config.setToleranceMax(getMergedCellValue(sheet, 2, 2, row2.getCell(2), evaluator, formatter));
                config.setToleranceMin(getMergedCellValue(sheet, 2, 3, row2.getCell(3), evaluator, formatter));
                config.setUpperLimit(getMergedCellValue(sheet, 2, 4, row2.getCell(4), evaluator, formatter));
                config.setLowerLimit(getMergedCellValue(sheet, 2, 5, row2.getCell(5), evaluator, formatter));
            }

            // 验证数据完整性
            if (!isValidConfigData(config)) {
                log.error("配置数据不完整");
                throw new RuntimeException("配置数据不完整,请检查Excel文件");
            }

            // 保存到数据库
            try {
                int result = mapper.insert(config);
                if (result > 0) {
                    log.info("配置数据保存成功");
                    printConfigData(config);
                    return result;
                } else {
                    log.error("配置数据保存失败");
                    throw new RuntimeException("配置数据保存失败");
                }
            } catch (Exception e) {
                log.error("保存配置数据时发生错误: {}", e.getMessage());
                throw new RuntimeException("保存配置数据失败: " + e.getMessage());
            }

        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败: " + e.getMessage());
        }
    }

    private boolean isValidConfigData(DfExcelC98bInkBm0Config01 config) {
        return config.getModelCode() != null
                && config.getMeasuringInstrument() != null
                && config.getNominalValue() != null
                && config.getToleranceMax() != null
                && config.getToleranceMin() != null
                && config.getUpperLimit() != null
                && config.getLowerLimit() != null;
    }

    private void printConfigData(DfExcelC98bInkBm0Config01 config) {
        log.info("==================== 配置数据 ====================");
        log.info("型号: {}", config.getModelCode());
        log.info("测量仪器: {}", config.getMeasuringInstrument());
        log.info("标称值: {}", config.getNominalValue());
        log.info("最大公差: {}", config.getToleranceMax());
        log.info("最小公差: {}", config.getToleranceMin());
        log.info("USL上限: {}", config.getUpperLimit());
        log.info("LSL下限: {}", config.getLowerLimit());
        log.info("================================================");
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        // 保持原有的合并单元格处理逻辑
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
            default:
                return null;
        }
    }

}




