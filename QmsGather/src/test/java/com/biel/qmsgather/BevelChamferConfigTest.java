package com.biel.qmsgather;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.math.BigDecimal;

@SpringBootTest
public class BevelChamferConfigTest {

    private static final Logger log = LoggerFactory.getLogger(BevelChamferConfigTest.class);

    @Test
    public void testReadConfigRows() {
        String fileName = "C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\2.CG MD 现有EXCEL导入模版\\C98B 精雕斜边+倒角- 9836-0.5 11-4...xlsx";

        try (FileInputStream fis = new FileInputStream(fileName);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 定义固定值
            String[] rowTypes = {"标准", "上限", "下限", "均值"};
            BigDecimal[] fixedValues = {
                    new BigDecimal("1.1"),  // 标准
                    new BigDecimal("1.3"),  // 上限
                    new BigDecimal("0.9"),  // 下限
                    null                    // 均值（实际值）
            };

            // 读取第3-6行（索引2-5）
            for (int rowIndex = 2; rowIndex <= 5; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                log.info("第{}行数据（{}）：", rowIndex + 1, rowTypes[rowIndex - 2]);

                // 处理前8列（B-I列，索引1-8）的固定值
                if (rowIndex <= 4) { // 第3-5行使用固定值
                    for (int i = 1; i <= 8; i++) {
                        log.info("列 {}: {}", i + 1, fixedValues[rowIndex - 2]);
                    }
                } else { // 第6行（均值）使用实际值
                    for (int i = 1; i <= 8; i++) {
                        Cell cell = row.getCell(i);
                        log.info("列 {}: {}", i + 1, getCellValueAsBigDecimal(cell));
                    }
                }

                // 处理后面的列（J-S列，索引9-18）
                for (int colIndex = 9; colIndex <= 18; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    Object value;

                    if (colIndex == 13 || colIndex == 14) { // N,O列 - 字符串
                        value = getCellValueAsString(cell);
                    } else if (colIndex == 18) { // S列 - 整数
                        value = getCellValueAsInteger(cell);
                    } else { // 其他列 - 小数
                        value = getCellValueAsBigDecimal(cell);
                    }

                    log.info("列 {}: {}", colIndex + 1, value);
                }
                log.info("------------------------");
            }

        } catch (Exception e) {
            log.error("读取Excel配置行失败", e);
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return null;
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
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        try {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) return null;
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }
}