package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkDepthConfig;

import com.biel.qmsgather.mapper.DfExcelC98bInkDepthConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkDepthConfigService;
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
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_depth_config】的数据库操作Service实现
* @createDate 2025-02-19 10:19:09
*/
@Service
@Slf4j
public class DfExcelC98bInkDepthConfigServiceImpl extends ServiceImpl<DfExcelC98bInkDepthConfigMapper, DfExcelC98bInkDepthConfig>
    implements DfExcelC98bInkDepthConfigService {


    @Autowired
    private DfExcelC98bInkDepthConfigMapper configMapper;


    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file, String batchId) {
        log.info("开始读取Excel配置文件...");
        List<DfExcelC98bInkDepthConfig> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("线框厚度");

            if (sheet == null) {
                log.error("未找到工作表: 线框厚度");
                throw new RuntimeException("未找到指定的工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 读取前5行数据
            for (int i = 0; i <= 4; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第{}行为空，跳过", i + 1);
                    continue;
                }

                DfExcelC98bInkDepthConfig config = new DfExcelC98bInkDepthConfig();

                config.setBatchId(batchId);

                // 读取前7列数据
                for (int j = 0; j < 7; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getMergedCellValue(sheet, i, j, cell, evaluator, formatter);

                    switch (j) {
                        case 0: config.setCol1(cellValue); break;
                        case 1: config.setCol2(cellValue); break;
                        case 2: config.setCol3(cellValue); break;
                        case 3: config.setCol4(cellValue); break;
                        case 4: config.setCol5(cellValue); break;
                        case 5: config.setCol6(cellValue); break;
                        case 6: config.setCol7(cellValue); break;
                    }
                }

                if (isValidConfigData(config)) {
                    configList.add(config);
                    log.debug("成功读取第{}行配置数据: {}", i + 1, config);
                } else {
                    log.warn("第{}行配置数据不完整，跳过: {}", i + 1, config);
                }
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(configList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bInkDepthConfig> configList) {
        if (configList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bInkDepthConfig config : configList) {
            try {
                configMapper.insert(config);
                successCount++;
                log.info("成功保存配置数据: {}", config);
            } catch (Exception e) {
                log.error("保存配置数据失败: {}, 错误: {}", config, e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell,
                                      FormulaEvaluator evaluator, DataFormatter formatter) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row, col)) {
                Cell firstCell = sheet.getRow(region.getFirstRow())
                        .getCell(region.getFirstColumn());
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
                return "";
            default:
                return null;
        }
    }

    private boolean isValidConfigData(DfExcelC98bInkDepthConfig config) {
        return config.getCol1() != null && !config.getCol1().isEmpty()
                && config.getCol2() != null
                && config.getCol3() != null
                && config.getCol4() != null
                && config.getCol5() != null
                && config.getCol6() != null
                && config.getCol7() != null;
    }


}




