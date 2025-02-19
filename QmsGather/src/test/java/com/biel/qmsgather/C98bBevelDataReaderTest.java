package com.biel.qmsgather;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @autor 96901
 * @date 2025/2/12
 */
@Slf4j
@SpringBootTest
public class C98bBevelDataReaderTest {





    @Test
    public void testReadExcel() {
        // 设置POI的安全限制
        ZipSecureFile.setMinInflateRatio(0.001);

        String fileName = "C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\2.CG MD 现有EXCEL导入模版\\C98B 精雕斜边+倒角- 9836-0.5 11-4...xlsx";
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            log.info("开始读取Excel文件：{}", fileName);
            fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);  // 获取第一个工作表

            // 从第8行开始读取（索引从7开始）
            int startRow = 7;
            int totalRows = sheet.getLastRowNum();
            log.info("Excel总行数：{}", totalRows + 1);

            for (int rowIndex = startRow; rowIndex <= totalRows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                try {
                    // 读取并记录每个字段的值
                    String timepoint = getCellValueAsString(row.getCell(0));
                    String leftLongEdgeBevel = getCellValueAsString(row.getCell(1));
                    String leftLongEdgeBevel1 = getCellValueAsString(row.getCell(2));
                    String lowerShortEdgeBevel = getCellValueAsString(row.getCell(3));
                    String lowerShortEdgeBevel1 = getCellValueAsString(row.getCell(4));
                    String rightLongEdgeBevel = getCellValueAsString(row.getCell(5));
                    String rightLongEdgeBevel1 = getCellValueAsString(row.getCell(6));
                    String upperShortEdgeBevel = getCellValueAsString(row.getCell(7));
                    String upperShortEdgeBevel1 = getCellValueAsString(row.getCell(8));
                    String grooveFaceChamfer = getCellValueAsString(row.getCell(9));
                    String longEdgeBaseChamfer = getCellValueAsString(row.getCell(10));
                    String shortEdgeBaseChamfer = getCellValueAsString(row.getCell(11));
                    String grooveBaseChamfer = getCellValueAsString(row.getCell(12));
                    String opportunityBow = getCellValueAsString(row.getCell(13));
                    String periodMachine = getCellValueAsString(row.getCell(14));
                    String totalValue = getCellValueAsString(row.getCell(15));
                    String bevelEdge = getCellValueAsString(row.getCell(16));
                    String chamfer = getCellValueAsString(row.getCell(17));
                    String count = getCellValueAsString(row.getCell(18));

                    // 打印读取到的值
                    log.info("第{}行数据: timepoint={}, 左长边倒角-1={}, 左长边倒角-2={}, " +
                                    "下短边倒角-1={}, 下短边倒角-2={}, 右长边倒角-1={}, 右长边倒角-2={}, " +
                                    "上短边倒角-1={}, 上短边倒角-2={}, 四槽面倒角={}, 长边底部倒角={}, " +
                                    "短边底部倒角={}, 四槽底倒角={}, 机台号={}, 检验类型={}, " +
                                    "斜边差值={}, 倒角差值={}, 残量次数={}",
                            rowIndex + 1, timepoint, leftLongEdgeBevel, leftLongEdgeBevel1,
                            lowerShortEdgeBevel, lowerShortEdgeBevel1, rightLongEdgeBevel, rightLongEdgeBevel1,
                            upperShortEdgeBevel, upperShortEdgeBevel1, grooveFaceChamfer, longEdgeBaseChamfer,
                            shortEdgeBaseChamfer, grooveBaseChamfer, opportunityBow, periodMachine,
                            totalValue, bevelEdge, chamfer, count);

                } catch (Exception e) {
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
            }
            log.info("Excel读取完成！");

        } catch (IOException e) {
            log.error("Excel文件读取失败：", e);
        } finally {
            closeResources(workbook, fis);
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
