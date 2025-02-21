package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkIr1Config;

import com.biel.qmsgather.mapper.DfExcelC98bInkIr1ConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkIr1ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir1_config】的数据库操作Service实现
* @createDate 2025-02-19 10:19:15
*/
@Service
@Slf4j
public class DfExcelC98bInkIr1ConfigServiceImpl extends ServiceImpl<DfExcelC98bInkIr1ConfigMapper, DfExcelC98bInkIr1Config>
    implements DfExcelC98bInkIr1ConfigService {

    @Autowired
    private DfExcelC98bInkIr1ConfigMapper configMapper;




    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importIr1ConfigFromExcel(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        List<DfExcelC98bInkIr1Config> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        int successCount = 0;
        int failCount = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());
            Sheet sheet = workbook.getSheet("IR1");

            if (sheet == null) {
                throw new RuntimeException("未找到IR1工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 读取前6行数据
            for (int i = 0; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第{}行为空，跳过", i + 1);
                    continue;
                }

                try {
                    DfExcelC98bInkIr1Config config = new DfExcelC98bInkIr1Config();
                    config.setBatchId(batchId);

                    // 读取前5列数据
                    for (int j = 0; j < 5; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = getMergedCellValue(sheet, i, j, cell, evaluator, formatter);

                        switch (j) {
                            case 0: config.setCol1(cellValue); break;
                            case 1: config.setCol2(cellValue); break;
                            case 2: config.setCol3(cellValue); break;
                            case 3: config.setCol4(cellValue); break;
                            case 4: config.setCol5(cellValue); break;
                        }
                    }

                    configList.add(config);
                    log.debug("成功读取第{}行数据: {}", i + 1, config);
                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!configList.isEmpty()) {
                successCount = batchSaveIr1Config(configList);
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (Exception e) {
            log.error("Excel导入失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveIr1Config(List<DfExcelC98bInkIr1Config> configList) {
        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bInkIr1Config config : configList) {
            try {
                configMapper.insert(config);
                successCount++;
                log.debug("成功保存配置数据: {}", config);
            } catch (Exception e) {
                log.error("保存配置数据失败: {}", e.getMessage());
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
                Cell firstCell = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
                return getCellValue(firstCell, evaluator, formatter);
            }
        }
        return getCellValue(cell, evaluator, formatter);
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) return "null";

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
                return "unknown";
        }
    }










}




