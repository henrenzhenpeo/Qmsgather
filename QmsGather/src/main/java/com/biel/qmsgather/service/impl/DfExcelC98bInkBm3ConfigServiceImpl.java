package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3Config;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm3ConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3ConfigService;
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
* @description 针对表【df_excel_c98b_ink_bm3_config】的数据库操作Service实现
* @createDate 2025-02-18 11:57:47
*/
@Service
@Slf4j
public class DfExcelC98bInkBm3ConfigServiceImpl extends ServiceImpl<DfExcelC98bInkBm3ConfigMapper, DfExcelC98bInkBm3Config>
    implements DfExcelC98bInkBm3ConfigService{


    @Autowired
    private DfExcelC98bInkBm3ConfigMapper configMapper;



    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file, String batchId) {
        List<DfExcelC98bInkBm3Config> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("BM1+BM2");

            if (sheet == null) {
                log.error("未找到工作表: BM1+BM2");
                return 0;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 遍历前6行数据
            for (int i = 0; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第 {} 行为空，跳过...", i + 1);
                    continue;
                }

                try {
                    DfExcelC98bInkBm3Config config = new DfExcelC98bInkBm3Config();

                    // 获取前5列的数据
                    String[] cellValues = new String[5];
                    for (int j = 0; j < 5; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        cellValues[j] = getMergedCellValue(sheet, i, j, cell, evaluator, formatter);
                    }


                    config.setBatchId(batchId);
                    // 设置实体类属性
                    config.setCol1(cellValues[0]);
                    config.setCol2(cellValues[1]);
                    config.setCol3(cellValues[2]);
                    config.setCol4(cellValues[3]);
                    config.setCol5(cellValues[4]);

                    if (isValidConfigData(config)) {
                        configList.add(config);
                        log.debug("成功读取第{}行配置数据: {}", i + 1, config);
                    } else {
                        log.warn("第{}行配置数据不完整，跳过: {}", i + 1, config);
                    }

                } catch (Exception e) {
                    log.error("读取第{}行配置数据失败: {}", i + 1, e.getMessage());
                }
            }

            workbook.close();

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(configList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bInkBm3Config> configList) {
        if (configList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bInkBm3Config config : configList) {
            try {
                configMapper.insert(config);
                successCount++;
                log.debug("成功保存配置数据: {}", config);
            } catch (Exception e) {
                log.error("保存配置数据失败: {}, 错误: {}", config, e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bInkBm3Config config) {
        return config.getCol1() != null && !config.getCol1().isEmpty()
                && config.getCol2() != null && !config.getCol2().isEmpty()
                && config.getCol3() != null && !config.getCol3().isEmpty()
                && config.getCol4() != null && !config.getCol4().isEmpty()
                && config.getCol5() != null && !config.getCol5().isEmpty();
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell,
                                      FormulaEvaluator evaluator, DataFormatter formatter) {
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
                return "";
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return null;
        }
    }

}




