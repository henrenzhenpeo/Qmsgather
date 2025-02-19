package com.biel.qmsgather;

import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsConfig;
import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsData;
import com.biel.qmsgather.mapper.DfExcelC98bBevelChamferMetricsConfigMapper;
import com.biel.qmsgather.mapper.DfExcelC98bBevelChamferMetricsDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
public class C98bBevelDataReaderTestPr {




    @Test
    public void readExcelRows() {
        try {
            // 读取文件
            FileInputStream file = new FileInputStream(new File("C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\已经完成\\2.CG MD 现有EXCEL导入模版\\C98B 精雕斜边+倒角- 9836-0.5 11-4...xlsx"));

            // 创建工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // 获取指定工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 遍历第三行到第六行
            for (int rowIndex = 2; rowIndex < 6; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    // 遍历行中的单元格
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex);
                        if (cell != null) {
                            // 根据单元格类型获取值并输出
                            switch (cell.getCellType()) {
                                case STRING:
                                    System.out.print(cell.getStringCellValue() + "\t");
                                    break;
                                case NUMERIC:
                                    System.out.print(cell.getNumericCellValue() + "\t");
                                    break;
                                case BOOLEAN:
                                    System.out.print(cell.getBooleanCellValue() + "\t");
                                    break;
                                default:
                                    System.out.print("\t");
                            }
                        } else {
                            System.out.print("\t");
                        }
                    }
                    System.out.println();
                }
            }

            // 关闭工作簿
            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testReadExcel() {
        String fileName = "C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\2.CG MD 现有EXCEL导入模版\\C98B 精雕斜边+倒角- 9836-0.5 11-4...xlsx";

        try (FileInputStream fis = new FileInputStream(fileName);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表

            // 读取第三行到第六行（索引从0开始，所以第三行是索引2，第六行是索引5）
            for (int i = 2; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            System.out.print(getCellValueAsString(cell) + "\t");
                        } else {
                            System.out.print("\t");
                        }
                    }
                    System.out.println();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }






}
