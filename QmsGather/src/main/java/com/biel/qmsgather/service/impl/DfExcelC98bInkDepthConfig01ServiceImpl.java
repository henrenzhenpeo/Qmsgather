package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkDepthConfig01;
import com.biel.qmsgather.mapper.DfExcelC98bInkDepthConfig01Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkDepthConfig01Service;
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
* @description 针对表【df_excel_c98b_ink_depth_config_01】的数据库操作Service实现
* @createDate 2025-02-21 10:03:14
*/
@Service
@Slf4j
public class DfExcelC98bInkDepthConfig01ServiceImpl extends ServiceImpl<DfExcelC98bInkDepthConfig01Mapper, DfExcelC98bInkDepthConfig01>
    implements DfExcelC98bInkDepthConfig01Service{

    @Autowired
    private DfExcelC98bInkDepthConfig01Mapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        log.info("开始读取Excel配置文件...");
        ZipSecureFile.setMinInflateRatio(0.001);

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("线框厚度");

            if (sheet == null) {
                log.error("未找到工作表: 线框厚度");
                throw new RuntimeException("未找到指定的工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 创建实体对象
            DfExcelC98bInkDepthConfig01 config = new DfExcelC98bInkDepthConfig01();

            config.setBatchId(batchId);

            // 读取第二行数据
            Row row2 = sheet.getRow(1);
            if (row2 != null) {
                config.setModelCode(getMergedCellValue(sheet, 1, 0, row2.getCell(0), evaluator, formatter));
                config.setMeasuringInstrument(getMergedCellValue(sheet, 1, 1, row2.getCell(1), evaluator, formatter));
                log.debug("读取型号: {}, 测量仪器: {}", config.getModelCode(), config.getMeasuringInstrument());
            }

            // 读取第四行数据
            Row row3 = sheet.getRow(2);
            if (row3 != null) {
                config.setNominalValue(getMergedCellValue(sheet, 2, 2, row3.getCell(2), evaluator, formatter));
                config.setToleranceMax(getMergedCellValue(sheet, 2, 3, row3.getCell(3), evaluator, formatter));
                config.setToleranceMin(getMergedCellValue(sheet, 2, 4, row3.getCell(4), evaluator, formatter));
                config.setUpperLimit(getMergedCellValue(sheet, 2, 5, row3.getCell(5), evaluator, formatter));
                config.setLowerLimit(getMergedCellValue(sheet, 2, 6, row3.getCell(6), evaluator, formatter));
            }

            if (isValidConfigData(config)) {
                // 打印配置信息
                printConfigInfo(config);
                // 保存到数据库
                int result = mapper.insert(config);
                log.info("配置数据保存完成，结果: {}", result > 0 ? "成功" : "失败");
                return result;
            } else {
                log.error("配置数据不完整");
                throw new RuntimeException("配置数据不完整");
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }
    }

    private boolean isValidConfigData(DfExcelC98bInkDepthConfig01 config) {
        return config.getModelCode() != null && !config.getModelCode().isEmpty()
                && config.getMeasuringInstrument() != null && !config.getMeasuringInstrument().isEmpty()
                && config.getNominalValue() != null
                && config.getToleranceMax() != null
                && config.getToleranceMin() != null
                && config.getUpperLimit() != null
                && config.getLowerLimit() != null;
    }

    private void printConfigInfo(DfExcelC98bInkDepthConfig01 config) {
        log.info("==================== 配置数据信息 ====================");
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




