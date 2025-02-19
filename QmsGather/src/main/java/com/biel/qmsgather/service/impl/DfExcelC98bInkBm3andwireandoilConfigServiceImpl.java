package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwireandoilConfig;

import com.biel.qmsgather.mapper.DfExcelC98bInkBm3andwireandoilConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3andwireandoilConfigService;
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
* @description 针对表【df_excel_c98b_ink_bm3andwireandoil_config】的数据库操作Service实现
* @createDate 2025-02-19 10:19:00
*/
@Service
@Slf4j
public class DfExcelC98bInkBm3andwireandoilConfigServiceImpl extends ServiceImpl<DfExcelC98bInkBm3andwireandoilConfigMapper, DfExcelC98bInkBm3andwireandoilConfig>
    implements DfExcelC98bInkBm3andwireandoilConfigService {



    @Autowired
    private DfExcelC98bInkBm3andwireandoilConfigMapper configMapper;


    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file) {
        log.info("开始读取Excel配置文件...");
        List<DfExcelC98bInkBm3andwireandoilConfig> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            String sheetName = "BM1+BM2+线框+光油";
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                log.error("未找到工作表: {}", sheetName);
                return 0;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 遍历前5行数据
            for (int i = 0; i <= 4; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第 {} 行为空，跳过...", i + 1);
                    continue;
                }

                DfExcelC98bInkBm3andwireandoilConfig config = new DfExcelC98bInkBm3andwireandoilConfig();

                try {
                    // 获取前11列的数据
                    for (int j = 0; j < 11 && j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = getMergedCellValue(sheet, i, j, cell, evaluator, formatter);

                        // 根据列索引设置对应的属性
                        switch (j) {
                            case 0: config.setCol1(cellValue); break;
                            case 1: config.setCol2(cellValue); break;
                            case 2: config.setCol3(cellValue); break;
                            case 3: config.setCol4(cellValue); break;
                            case 4: config.setCol5(cellValue); break;
                            case 5: config.setCol6(cellValue); break;
                            case 6: config.setCol7(cellValue); break;
                            case 7: config.setCol8(cellValue); break;
                            case 8: config.setCol9(cellValue); break;
                            case 9: config.setCol10(cellValue); break;
                            case 10: config.setCol11(cellValue); break;
                        }
                    }

                    if (isValidConfigData(config)) {
                        configList.add(config);
                        log.debug("成功读取第 {} 行配置数据", i + 1);
                    } else {
                        log.warn("第 {} 行配置数据不完整，跳过", i + 1);
                    }

                } catch (Exception e) {
                    log.error("处理第 {} 行数据时发生错误: {}", i + 1, e.getMessage());
                }
            }

        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(configList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bInkBm3andwireandoilConfig> configList) {
        if (configList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bInkBm3andwireandoilConfig config : configList) {
            try {
                configMapper.insert(config);
                successCount++;
                log.debug("成功保存第 {} 条配置数据", successCount);
            } catch (Exception e) {
                log.error("保存配置数据失败: {}", e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存 {} 条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bInkBm3andwireandoilConfig config) {
        return config.getCol1() != null && !config.getCol1().isEmpty()
                && config.getCol2() != null
                && config.getCol3() != null
                && config.getCol4() != null
                && config.getCol5() != null;
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
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return null;
        }
    }


}




