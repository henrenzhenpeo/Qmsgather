package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bRCornerGrooveConfig;
import com.biel.qmsgather.mapper.DfExcelC98bRCornerGrooveConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bRCornerGrooveConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
* @description 针对表【df_excel_c98b_r_corner_groove_config(C98B线框R角与凹槽相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:12:55
*/
@Service
@Slf4j
public class DfExcelC98bRCornerGrooveConfigServiceImpl extends ServiceImpl<DfExcelC98bRCornerGrooveConfigMapper, DfExcelC98bRCornerGrooveConfig>
    implements DfExcelC98bRCornerGrooveConfigService{


    @Autowired
    private DfExcelC98bRCornerGrooveConfigMapper cornerGrooveConfigMapper;


    @Override
    public int importExcelData(MultipartFile file) {
        List<DfExcelC98bRCornerGrooveConfig> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            try {
                Sheet sheet = workbook.getSheetAt(0);
                log.info("开始读取Excel配置数据，处理第5-7行");

                // 读取第五行到第七行
                for (int rowNum = 4; rowNum < 7; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) continue;

                    DfExcelC98bRCornerGrooveConfig config = new DfExcelC98bRCornerGrooveConfig();

                    try {
                        // 设置各个字段的值
                        config.setStandardType(getCellValueAsString(row.getCell(0)));

                        // R角数据
                        config.setR8(getCellValueAsBigDecimal(row.getCell(8)));
                        config.setR1(getCellValueAsBigDecimal(row.getCell(9)));
                        config.setR2(getCellValueAsBigDecimal(row.getCell(10)));
                        config.setR3(getCellValueAsBigDecimal(row.getCell(11)));
                        config.setR4(getCellValueAsBigDecimal(row.getCell(12)));

                        // 凹槽R角数据
                        config.setRightOutside(getCellValueAsBigDecimal(row.getCell(13)));
                        config.setRightInside(getCellValueAsBigDecimal(row.getCell(14)));
                        config.setLeftInside(getCellValueAsBigDecimal(row.getCell(15)));
                        config.setLeftOutside(getCellValueAsBigDecimal(row.getCell(16)));

                        // 凹槽数据
                        config.setGroove1(getCellValueAsBigDecimal(row.getCell(17)));
                        config.setGroove2(getCellValueAsBigDecimal(row.getCell(18)));
                        config.setGroove3(getCellValueAsBigDecimal(row.getCell(19)));
                        config.setGroove4(getCellValueAsBigDecimal(row.getCell(20)));
                        config.setGroove5(getCellValueAsBigDecimal(row.getCell(21)));

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
    public int batchSaveConfig(List<DfExcelC98bRCornerGrooveConfig> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bRCornerGrooveConfig data : dataList) {
            try {
                cornerGrooveConfigMapper.insert(data);
                successCount++;
                log.info("成功保存配置数据: {}", data.getStandardType());
            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", data.getStandardType(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bRCornerGrooveConfig config) {
        return config.getStandardType() != null && !config.getStandardType().isEmpty()
                && config.getR8() != null
                && config.getR1() != null
                && config.getR2() != null
                && config.getR3() != null
                && config.getR4() != null
                && config.getRightOutside() != null
                && config.getRightInside() != null
                && config.getLeftInside() != null
                && config.getLeftOutside() != null
                && config.getGroove1() != null
                && config.getGroove2() != null
                && config.getGroove3() != null
                && config.getGroove4() != null
                && config.getGroove5() != null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

}




