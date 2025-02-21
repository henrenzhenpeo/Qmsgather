package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm1Config;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm1ConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm1ConfigService;
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
* @description 针对表【df_excel_c98b_ink_bm1_config】的数据库操作Service实现
* @createDate 2025-02-18 11:57:34
*/
@Slf4j
@Service
public class DfExcelC98bInkBm1ConfigServiceImpl extends ServiceImpl<DfExcelC98bInkBm1ConfigMapper, DfExcelC98bInkBm1Config>
    implements DfExcelC98bInkBm1ConfigService{



    @Autowired
    private DfExcelC98bInkBm1ConfigMapper configMapper;



    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file, String batchId) {
        List<DfExcelC98bInkBm1Config> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("BM1");

            if (sheet == null) {
                log.error("未找到BM1工作表");
                throw new RuntimeException("未找到BM1工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            log.info("开始读取Excel配置数据，处理前6行");

            // 读取前6行数据
            for (int i = 0; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第{}行为空，跳过", i + 1);
                    continue;
                }

                DfExcelC98bInkBm1Config config = new DfExcelC98bInkBm1Config();

                try {
                    config.setBatchId(batchId);
                    // 读取前5列的数据
                    config.setCol1(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    config.setCol2(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    config.setCol3(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    config.setCol4(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    config.setCol5(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));

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
    public int batchSaveConfig(List<DfExcelC98bInkBm1Config> configList) {
        if (configList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bInkBm1Config config : configList) {
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

    private boolean isValidConfigData(DfExcelC98bInkBm1Config config) {
        return config.getCol1() != null && !config.getCol1().isEmpty()
                && config.getCol2() != null && !config.getCol2().isEmpty()
                && config.getCol3() != null && !config.getCol3().isEmpty()
                && config.getCol4() != null && !config.getCol4().isEmpty()
                && config.getCol5() != null && !config.getCol5().isEmpty();
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
                return "";
            default:
                return null;
        }
    }







}




