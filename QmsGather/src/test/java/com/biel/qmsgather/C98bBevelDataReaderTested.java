package com.biel.qmsgather;

import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsData;
import com.biel.qmsgather.mapper.DfExcelC98bBevelChamferMetricsDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor 96901
 * @date 2025/2/12
 */

@SpringBootTest
@Slf4j
public class C98bBevelDataReaderTested {


    @Autowired
    private DfExcelC98bBevelChamferMetricsDataMapper bevelDataMapper;

    @Test
    public void testReadAndSaveExcel() {
        ZipSecureFile.setMinInflateRatio(0.001);

        String fileName = "C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\2.CG MD 现有EXCEL导入模版\\C98B 精雕斜边+倒角- 9836-0.5 11-4...xlsx";
        FileInputStream fis = null;
        Workbook workbook = null;

        // 用于批量插入的列表
        List<DfExcelC98bBevelChamferMetricsData> dataList = new ArrayList<>();
        int batchSize = 100; // 每100条数据批量插入一次
        int successCount = 0;
        int failCount = 0;

        try {
            log.info("开始读取Excel文件：{}", fileName);
            fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);
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

                    // 打印读取到的数据
                    log.info("第{}行数据: {}", rowIndex + 1, data);

                    dataList.add(data);

                    // 达到批量大小时插入数据库
                    if (dataList.size() >= batchSize) {
                        successCount += insertBatch(dataList);
                        dataList.clear();
                    }

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
            }

            // 处理剩余的数据
            if (!dataList.isEmpty()) {
                successCount += insertBatch(dataList);
            }

            log.info("Excel处理完成！成功导入{}条数据，失败{}条", successCount, failCount);

        } catch (IOException e) {
            log.error("Excel文件读取失败：", e);
        } finally {
            closeResources(workbook, fis);
        }
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

    // 获取单元格的字符串值
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
                    // 使用BigDecimal避免科学计数法
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

    // 判断单元格是否为空
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

    // 判断行是否为空
    private boolean isEmptyRow(Row row) {
        for (int cellIndex = 0; cellIndex < 19; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }

    // 关闭资源
    private void closeResources(Workbook workbook, FileInputStream fis) {
        try {
            if (workbook != null) {
                workbook.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            log.error("关闭资源失败：", e);
        }
    }










}
