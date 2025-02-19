package com.biel.qmsgather;

import com.biel.qmsgather.domain.DfExcelC98bRCornerGroove;
import com.biel.qmsgather.mapper.DfExcelC98bRCornerGrooveMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class C98bCornerGrooveTested {

    @Autowired
    private DfExcelC98bRCornerGrooveMapper mapper;

    @Test
    public void testReadExcel() {


        String filePath = "C:\\Users\\96901\\Desktop\\项目需求\\Excel导入文档的上传模板\\2.CG MD 现有EXCEL导入模版\\C98B线框20240624（手动）.xls";


        List<DfExcelC98bRCornerGroove> dataList = new ArrayList<>();

        try {
            log.info("开始读取Excel文件: {}", filePath);
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);

            try {
                Sheet sheet = workbook.getSheetAt(0);
                log.info("成功打开工作表，总行数：{}", sheet.getLastRowNum());

                // 从第9行开始读取数据
                for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (isEmptyRow(row)) {
                        log.debug("第{}行为空行，跳过", i + 1);
                        continue;
                    }

                    try {
                        DfExcelC98bRCornerGroove data = readRowData(row);
                        dataList.add(data);
                        log.info("第{}行数据: {}", i + 1, formatDataToString(data));
                    } catch (Exception e) {
                        log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                    }
                }

                // 批量插入数据
                if (!dataList.isEmpty()) {
                    log.info("开始插入数据到数据库，总记录数：{}", dataList.size());
                    int successCount = 0;
                    for (DfExcelC98bRCornerGroove data : dataList) {
                        try {
                            mapper.insert(data);
                            successCount++;
                            log.debug("成功插入记录：{}", data.getRecordTime());
                        } catch (Exception e) {
                            log.error("插入记录失败：{}, 错误：{}", data.getRecordTime(), e.getMessage());
                        }
                    }
                    log.info("数据插入完成，成功：{}，失败：{}", successCount, dataList.size() - successCount);
                }

            } finally {
                workbook.close();
                fis.close();
                log.info("Excel文件读取完成");
            }

        } catch (IOException e) {
            log.error("打开Excel文件失败: {}", e.getMessage());
        }
    }

    private DfExcelC98bRCornerGroove readRowData(Row row) {
        DfExcelC98bRCornerGroove data = new DfExcelC98bRCornerGroove();

        // 读取时间
        data.setRecordTime(getCellValueAsString(row.getCell(0)));

        // 读取R角数据
        data.setR8(getCellValueAsBigDecimal(row.getCell(1)));
        data.setR1(getCellValueAsBigDecimal(row.getCell(2)));
        data.setR2(getCellValueAsBigDecimal(row.getCell(3)));
        data.setR3(getCellValueAsBigDecimal(row.getCell(4)));
        data.setR4(getCellValueAsBigDecimal(row.getCell(5)));

        // 读取凹槽R角数据
        data.setRightOutside(getCellValueAsBigDecimal(row.getCell(6)));
        data.setRightInside(getCellValueAsBigDecimal(row.getCell(7)));
        data.setLeftInside(getCellValueAsBigDecimal(row.getCell(8)));
        data.setLeftOutside(getCellValueAsBigDecimal(row.getCell(9)));

        // 读取凹槽数据
        data.setGroove1(getCellValueAsBigDecimal(row.getCell(10)));
        data.setGroove2(getCellValueAsBigDecimal(row.getCell(11)));
        data.setGroove3(getCellValueAsBigDecimal(row.getCell(12)));
        data.setGroove4(getCellValueAsBigDecimal(row.getCell(13)));
        data.setGroove5(getCellValueAsBigDecimal(row.getCell(14)));

        // 读取机台号
        Cell machineCell = row.getCell(15);
        if (machineCell != null) {
            if (machineCell.getCellType() == CellType.NUMERIC) {
                data.setMachineNumber((int) machineCell.getNumericCellValue());
            } else {
                String value = machineCell.getStringCellValue().trim();
                if (!value.isEmpty()) {
                    data.setMachineNumber(Integer.parseInt(value));
                }
            }
        }

        // 读取状态
        data.setStatus(getCellValueAsString(row.getCell(16)));

        // 读取备注
        data.setNoted(getCellValueAsString(row.getCell(17)));

        return data;
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) return true;

        for (int i = 0; i < 18; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("转换单元格值到BigDecimal失败: {}", e.getMessage());
            return null;
        }
    }

    private String formatDataToString(DfExcelC98bRCornerGroove data) {
        return String.format(
                "时间: %s, R8: %s, R1: %s, R2: %s, R3: %s, R4: %s, " +
                        "右外: %s, 右内: %s, 左内: %s, 左外: %s, " +
                        "凹槽1: %s, 凹槽2: %s, 凹槽3: %s, 凹槽4: %s, 凹槽5: %s, " +
                        "机台号: %s, 状态: %s, 备注: %s",
                data.getRecordTime(), data.getR8(), data.getR1(), data.getR2(),
                data.getR3(), data.getR4(), data.getRightOutside(), data.getRightInside(),
                data.getLeftInside(), data.getLeftOutside(), data.getGroove1(),
                data.getGroove2(), data.getGroove3(), data.getGroove4(), data.getGroove5(),
                data.getMachineNumber(), data.getStatus(), data.getNoted()
        );
    }
}