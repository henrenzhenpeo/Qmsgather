package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bLayer2Program20240624ManualConfig;
import com.biel.qmsgather.mapper.DfExcelC98bLayer2Program20240624ManualConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_layer2_program_20240624_manual_config(C98B二层程序20240624（手动）相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:12:37
*/
@Service
@Slf4j
public class DfExcelC98bLayer2Program20240624ManualConfigServiceImpl extends ServiceImpl<DfExcelC98bLayer2Program20240624ManualConfigMapper, DfExcelC98bLayer2Program20240624ManualConfig>
    implements DfExcelC98bLayer2Program20240624ManualConfigService{






    @Autowired
    private DfExcelC98bLayer2Program20240624ManualConfigMapper configMapper;


    public int importConfigFromExcel(MultipartFile file,String batchId) {
        List<DfExcelC98bLayer2Program20240624ManualConfig> configList = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            try {
                Sheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                log.info("开始读取Excel配置数据，处理第3-8行");

                // 存储标准类型的映射
                String[] standardTypes = {"位置", "标准", "上限", "下限", "QCP上限", "QCP下限"};

                for (int rowNum = 2; rowNum < 8; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) continue;

                    DfExcelC98bLayer2Program20240624ManualConfig config = new DfExcelC98bLayer2Program20240624ManualConfig();

                    try {
                        config.setStandandType(standardTypes[rowNum - 2]);

                        // 从B列开始读取数据（跳过A列）
                        for (int colNum = 1; colNum <= 10; colNum++) {
                            Cell cell = row.getCell(colNum);
                            String value = getCellValueAsString(cell, evaluator);

                            // 根据列号设置对应的position字段
                            switch (colNum) {
                                case 1: config.setPosition28(value); break;
                                case 2: config.setPosition29(value); break;
                                case 3: config.setPosition30(value); break;
                                case 4: config.setPosition31(value); break;
                                case 5: config.setPosition32(value); break;
                                case 6: config.setPosition33(value); break;
                                case 7: config.setPosition34(value); break;
                                case 8: config.setPosition35(value); break;
                                case 9: config.setPosition36(value); break;
                                case 10: config.setPosition37(value); break;
                            }
                        }

                        if (isValidConfigData(config)) {
                            configList.add(config);
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

        return batchSaveConfig(configList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bLayer2Program20240624ManualConfig> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bLayer2Program20240624ManualConfig config : dataList) {
            try {
                configMapper.insert(config);
                successCount++;
                log.info("成功保存配置数据: {}", config.getStandandType());
            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", config.getStandandType(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bLayer2Program20240624ManualConfig config) {
        return config.getStandandType() != null && !config.getStandandType().isEmpty()
                && config.getPosition28() != null
                && config.getPosition29() != null
                && config.getPosition30() != null
                && config.getPosition31() != null
                && config.getPosition32() != null
                && config.getPosition33() != null
                && config.getPosition34() != null
                && config.getPosition35() != null
                && config.getPosition36() != null
                && config.getPosition37() != null;
    }

    private String getCellValueAsString(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return "";

        CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) return "";

        switch (cellValue.getCellType()) {
            case NUMERIC:
                double numValue = cellValue.getNumberValue();
                // 如果是整数，去掉小数部分
                if (numValue == Math.floor(numValue)) {
                    return String.format("%.0f", numValue);
                }
                return String.valueOf(numValue);
            case STRING:
                return cellValue.getStringValue();
            case BOOLEAN:
                return String.valueOf(cellValue.getBooleanValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}




