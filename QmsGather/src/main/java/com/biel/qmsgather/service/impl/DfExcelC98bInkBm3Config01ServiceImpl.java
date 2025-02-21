package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3Config01;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm3Config01Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3Config01Service;
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
* @description 针对表【df_excel_c98b_ink_bm3_config_01】的数据库操作Service实现
* @createDate 2025-02-21 10:02:57
*/
@Service
@Slf4j
public class DfExcelC98bInkBm3Config01ServiceImpl extends ServiceImpl<DfExcelC98bInkBm3Config01Mapper, DfExcelC98bInkBm3Config01>
    implements DfExcelC98bInkBm3Config01Service{


    @Autowired
    private DfExcelC98bInkBm3Config01Mapper mapper;

    public int importConfigFromExcel(MultipartFile file, String batchId) {
        log.info("开始读取Excel文件...");
        List<DfExcelC98bInkBm3Config01> dataList = new ArrayList<>();

        try {
            ZipSecureFile.setMinInflateRatio(0.001);
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("BM1+BM2");

            if (sheet == null) {
                log.error("工作表未找到: BM1+BM2");
                return 0;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            try {
                // 创建实体对象
                DfExcelC98bInkBm3Config01 config = new DfExcelC98bInkBm3Config01();

                config.setBatchId(batchId);

                // 读取第二行数据
                Row row2 = sheet.getRow(1);
                if (row2 != null) {
                    config.setModelCode(getMergedCellValue(sheet, 1, 0, row2.getCell(0), evaluator, formatter));
                    config.setMeasuringInstrument(getMergedCellValue(sheet, 1, 1, row2.getCell(1), evaluator, formatter));
                    log.info("型号: {}, 测量仪器: {}", config.getModelCode(), config.getMeasuringInstrument());
                }

                // 读取第四行数据
                Row row4 = sheet.getRow(3);
                if (row4 != null) {
                    config.setNominalValue(getMergedCellValue(sheet, 3, 0, row4.getCell(0), evaluator, formatter));
                    config.setToleranceMax(getMergedCellValue(sheet, 3, 1, row4.getCell(1), evaluator, formatter));
                    config.setToleranceMin(getMergedCellValue(sheet, 3, 2, row4.getCell(2), evaluator, formatter));
                    config.setUpperLimit(getMergedCellValue(sheet, 3, 3, row4.getCell(3), evaluator, formatter));
                    config.setLowerLimit(getMergedCellValue(sheet, 3, 4, row4.getCell(4), evaluator, formatter));

                    if (isValidConfigData(config)) {
                        dataList.add(config);
                        log.info("读取到的配置信息: {}", config);
                    } else {
                        log.warn("配置数据不完整，跳过");
                    }
                }

            } finally {
                workbook.close();
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bInkBm3Config01> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkBm3Config01 data : dataList) {
            try {
                mapper.insert(data);
                successCount++;
                log.info("成功保存配置数据: {}", data);
            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", data.getModelCode(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bInkBm3Config01 data) {
        return data.getModelCode() != null && !data.getModelCode().isEmpty()
                && data.getMeasuringInstrument() != null
                && data.getNominalValue() != null
                && data.getToleranceMax() != null
                && data.getToleranceMin() != null
                && data.getUpperLimit() != null
                && data.getLowerLimit() != null;
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




