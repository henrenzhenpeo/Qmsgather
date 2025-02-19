package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsConfig;
import com.biel.qmsgather.mapper.DfExcelC98bBevelChamferMetricsConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bBevelChamferMetricsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_bevel_chamfer_metrics_config】的数据库操作Service实现
* @createDate 2025-02-12 11:55:09
*/
@Service
@Slf4j
public class DfExcelC98bBevelChamferMetricsConfigServiceImpl extends ServiceImpl<DfExcelC98bBevelChamferMetricsConfigMapper, DfExcelC98bBevelChamferMetricsConfig>
    implements DfExcelC98bBevelChamferMetricsConfigService{




    @Autowired
    private DfExcelC98bBevelChamferMetricsConfigMapper dfExcelC98bBevelChamferMetricsConfigMapper;


    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file) {
        List<DfExcelC98bBevelChamferMetricsConfig> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            try {
                Sheet sheet = workbook.getSheetAt(0);
                log.info("开始读取Excel配置数据，处理第3-6行");

                // 遍历第3-6行（索引2-5）
                for (int rowNum = 2; rowNum <= 5; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) continue;

                    try {
                        DfExcelC98bBevelChamferMetricsConfig config = new DfExcelC98bBevelChamferMetricsConfig();

                        // 设置类型（标准/上限/下限/均值）
                        String[] types = {"标准", "上限", "下限", "均值"};
                        config.setTypeStandard(types[rowNum - 2]);

                        // 读取所有列的数据
                        config.setLeftLongEdgeBevel(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setLeftLongEdgeBevel1(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setLowerShortEdgeBevel(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setLowerShortEdgeBevel1(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setRightLongEdgeBevel(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setRightLongEdgeBevel1(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setUpperShortEdgeBevel(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setUpperShortEdgeBevel1(getBigDecimalValue(row.getCell(1), evaluator));
                        config.setGrooveFaceChamfer(getBigDecimalValue(row.getCell(9), evaluator));
                        config.setLongEdgeBaseChamfer(getBigDecimalValue(row.getCell(10), evaluator));
                        config.setShortEdgeBaseChamfer(getBigDecimalValue(row.getCell(11), evaluator));
                        config.setGrooveBaseChamfer(getBigDecimalValue(row.getCell(12), evaluator));
                        config.setOpportunityBow(getCellValueAsString(row.getCell(13), evaluator));
                        config.setPeriodMachine(getCellValueAsString(row.getCell(14), evaluator));
                        config.setBevelEdge(getBigDecimalValue(row.getCell(15), evaluator));
                        config.setChamfer(getBigDecimalValue(row.getCell(16), evaluator));

                        // 处理count字段
                        String countStr = getCellValueAsString(row.getCell(17), evaluator);
                        config.setCount(countStr.contains(".") ?
                                (int) Double.parseDouble(countStr) :
                                Integer.parseInt(countStr));

                        if (isValidConfigData(config)) {
                            dataList.add(config);
                            log.debug("成功读取第{}行配置数据: {}", rowNum + 1, config);
                        } else {
                            log.warn("第{}行配置数据不完整，跳过: {}", rowNum + 1, config);
                        }

                    } catch (Exception e) {
                        log.error("读取第{}行配置数据失败: {}", rowNum + 1, e.getMessage());
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
    public int batchSaveConfig(List<DfExcelC98bBevelChamferMetricsConfig> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bBevelChamferMetricsConfig config : dataList) {
            try {
                dfExcelC98bBevelChamferMetricsConfigMapper.insert(config);
                successCount++;

                // 记录详细日志
                if ("均值".equals(config.getTypeStandard())) {
                    log.info("==================== 均值配置数据 ====================");
                    log.info("左长边斜边1: {}", config.getLeftLongEdgeBevel());
                    log.info("左长边斜边2: {}", config.getLeftLongEdgeBevel1());
                    log.info("下短边斜边1: {}", config.getLowerShortEdgeBevel());
                    log.info("下短边斜边2: {}", config.getLowerShortEdgeBevel1());
                    log.info("右长边斜边1: {}", config.getRightLongEdgeBevel());
                    log.info("右长边斜边2: {}", config.getRightLongEdgeBevel1());
                    log.info("上短边斜边1: {}", config.getUpperShortEdgeBevel());
                    log.info("上短边斜边2: {}", config.getUpperShortEdgeBevel1());
                    log.info("槽面倒角: {}", config.getGrooveFaceChamfer());
                    log.info("长边底面倒角: {}", config.getLongEdgeBaseChamfer());
                    log.info("短边底面倒角: {}", config.getShortEdgeBaseChamfer());
                    log.info("槽底倒角: {}", config.getGrooveBaseChamfer());
                    log.info("机会弓: {}", config.getOpportunityBow());
                    log.info("周期机台: {}", config.getPeriodMachine());
                    log.info("总值: {}", config.getTotalValue());
                    log.info("斜边: {}", config.getBevelEdge());
                    log.info("倒角: {}", config.getChamfer());
                    log.info("数量: {}", config.getCount());
                    log.info("================================================");
                }

            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", config.getTypeStandard(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bBevelChamferMetricsConfig config) {
        return config.getTypeStandard() != null
                && config.getLeftLongEdgeBevel() != null
                && config.getLeftLongEdgeBevel1() != null
                && config.getLowerShortEdgeBevel() != null
                && config.getLowerShortEdgeBevel1() != null
                && config.getRightLongEdgeBevel() != null
                && config.getRightLongEdgeBevel1() != null
                && config.getUpperShortEdgeBevel() != null
                && config.getUpperShortEdgeBevel1() != null
                && config.getGrooveFaceChamfer() != null
                && config.getLongEdgeBaseChamfer() != null
                && config.getShortEdgeBaseChamfer() != null
                && config.getGrooveBaseChamfer() != null;
    }

    private String getCellValueAsString(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return "0";
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cell = evaluator.evaluateInCell(cell);
            cellType = cell.getCellType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue())
                        .stripTrailingZeros()
                        .toPlainString();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "0";
            default:
                return "0";
        }
    }

    private BigDecimal getBigDecimalValue(Cell cell, FormulaEvaluator evaluator) {
        String value = getCellValueAsString(cell, evaluator);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

}




