package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelProcesscontroldata;
import com.biel.qmsgather.mapper.DfExcelProcesscontroldataMapper;
import com.biel.qmsgather.service.DfExcelProcesscontroldataService;
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

/**
* @author 96901
* @description 针对表【df_excel_processcontroldata】的数据库操作Service实现
* @createDate 2025-02-12 09:44:14
*/
@Service
@Slf4j
public class DfExcelProcesscontroldataServiceImpl extends ServiceImpl<DfExcelProcesscontroldataMapper, DfExcelProcesscontroldata>
    implements DfExcelProcesscontroldataService{


    @Autowired
    private DfExcelProcesscontroldataMapper processcontroldataMapper;









    public Map<String, Object> importExcelData(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        List<DfExcelProcesscontroldata> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (InputStream fis = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(fis)) {

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());
            Sheet sheet = workbook.getSheetAt(0);

            int startRow = 8;  // 从第9行开始读取
            int totalRows = sheet.getLastRowNum();
            log.info("Excel总行数：{}", totalRows + 1);

            for (int rowIndex = startRow; rowIndex <= totalRows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                try {
                    DfExcelProcesscontroldata data = new DfExcelProcesscontroldata();

                    data.setBatchId(batchId);

                    // 读取并设置每个字段的值
                    data.setTimepoint(getCellValueAsString(row.getCell(0)));
                    data.setOuterlengthupper(getCellValueAsBigDecimal(row.getCell(1)));
                    data.setOuterlengthlower(getCellValueAsBigDecimal(row.getCell(2)));
                    data.setOuterwidthupper(getCellValueAsBigDecimal(row.getCell(3)));
                    data.setOuterwidthlower(getCellValueAsBigDecimal(row.getCell(4)));
                    data.setInlayheight(getCellValueAsBigDecimal(row.getCell(5)));
                    data.setInlaywidth(getCellValueAsBigDecimal(row.getCell(6)));
                    data.setInlaybottomtoglassbottom(getCellValueAsBigDecimal(row.getCell(7)));
                    data.setInlaytoglasscenter(getCellValueAsBigDecimal(row.getCell(8)));
                    data.setInlaytoptoglasstop(getCellValueAsBigDecimal(row.getCell(9)));
                    data.setInlaybottomtoglassbottomdistance(getCellValueAsBigDecimal(row.getCell(10)));
                    data.setMachinenumber(getCellValueAsString(row.getCell(11)));
                    data.setNotes(getCellValueAsString(row.getCell(12)));
                    data.setWorkstation(getCellValueAsString(row.getCell(13)));

                    // 保存到数据库
                    processcontroldataMapper.insert(data);
                    successCount++;
                    log.info("第{}行数据保存成功", rowIndex + 1);

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
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

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }
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
            log.error("转换BigDecimal失败，单元格值：{}", cell, e);
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
        for (int cellIndex = 0; cellIndex < 14; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }





}




