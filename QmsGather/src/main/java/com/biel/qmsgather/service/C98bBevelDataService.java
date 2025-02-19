package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsData;
import com.biel.qmsgather.mapper.DfExcelC98bBevelChamferMetricsDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class C98bBevelDataService {

    @Autowired
    private DfExcelC98bBevelChamferMetricsDataMapper bevelDataMapper;

    public Map<String, Object> importExcelData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        List<DfExcelC98bBevelChamferMetricsData> dataList = new ArrayList<>();
        int batchSize = 100;
        int successCount = 0;
        int failCount = 0;

        try (InputStream fis = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(fis)) {

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());
            Sheet sheet = workbook.getSheetAt(0);

            int startRow = 7;
            int totalRows = sheet.getLastRowNum();
            log.info("Excel总行数：{}", totalRows + 1);

            for (int rowIndex = startRow; rowIndex <= totalRows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                try {
                    DfExcelC98bBevelChamferMetricsData data = new DfExcelC98bBevelChamferMetricsData();

                    // 设置各个字段的值
                    data.setTimepoint(getCellValueAsString(row.getCell(0)));
                    data.setLeftLongEdgeBevel(getCellValueAsBigDecimal(row.getCell(1)));
                    data.setLeftLongEdgeBevel1(getCellValueAsBigDecimal(row.getCell(2)));
                    data.setLowerShortEdgeBevel(getCellValueAsBigDecimal(row.getCell(3)));
                    data.setLowerShortEdgeBevel1(getCellValueAsBigDecimal(row.getCell(4)));
                    data.setRightLongEdgeBevel(getCellValueAsBigDecimal(row.getCell(5)));
                    data.setRightLongEdgeBevel1(getCellValueAsBigDecimal(row.getCell(6)));
                    data.setUpperShortEdgeBevel(getCellValueAsBigDecimal(row.getCell(7)));
                    data.setUpperShortEdgeBevel1(getCellValueAsBigDecimal(row.getCell(8)));
                    data.setGrooveFaceChamfer(getCellValueAsBigDecimal(row.getCell(9)));
                    data.setLongEdgeBaseChamfer(getCellValueAsBigDecimal(row.getCell(10)));
                    data.setShortEdgeBaseChamfer(getCellValueAsBigDecimal(row.getCell(11)));
                    data.setGrooveBaseChamfer(getCellValueAsBigDecimal(row.getCell(12)));
                    data.setOpportunityBow(getCellValueAsString(row.getCell(13)));
                    data.setPeriodMachine(getCellValueAsString(row.getCell(14)));
                    data.setTotalValue(getCellValueAsBigDecimal(row.getCell(15)));
                    data.setBevelEdge(getCellValueAsBigDecimal(row.getCell(16)));
                    data.setChamfer(getCellValueAsBigDecimal(row.getCell(17)));
                    data.setCount(getCellValueAsInteger(row.getCell(18)));

                    dataList.add(data);

                    if (dataList.size() >= batchSize) {
                        successCount += insertBatch(dataList);
                        dataList.clear();
                    }

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
            }

            if (!dataList.isEmpty()) {
                successCount += insertBatch(dataList);
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("Excel处理完成！成功导入%d条数据，失败%d条", successCount, failCount));

        } catch (IOException e) {
            log.error("Excel文件读取失败：", e);
            result.put("success", false);
            result.put("message", "Excel文件读取失败：" + e.getMessage());
        }

        return result;
    }

    private int insertBatch(List<DfExcelC98bBevelChamferMetricsData> dataList) {
        int successCount = 0;
        for (DfExcelC98bBevelChamferMetricsData data : dataList) {
            try {
                bevelDataMapper.insert(data);
                successCount++;
            } catch (Exception e) {
                log.error("数据插入失败：{}", data, e);
            }
        }
        return successCount;
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue())
                            .setScale(3, BigDecimal.ROUND_HALF_UP);
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value)
                            .setScale(3, BigDecimal.ROUND_HALF_UP);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("数值转换失败，单元格值：{}", cell, e);
            return null;
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : Integer.parseInt(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("整数转换失败，单元格值：{}", cell, e);
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cell.getDateCellValue());
                    }
                    return BigDecimal.valueOf(cell.getNumericCellValue())
                            .stripTrailingZeros().toPlainString();
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("获取单元格值失败：{}", e.getMessage());
            return null;
        }
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim().isEmpty();
            case BLANK:
                return true;
            case NUMERIC:
                return false;
            default:
                return true;
        }
    }

    private boolean isEmptyRow(Row row) {
        for (int cellIndex = 0; cellIndex < 19; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }
}