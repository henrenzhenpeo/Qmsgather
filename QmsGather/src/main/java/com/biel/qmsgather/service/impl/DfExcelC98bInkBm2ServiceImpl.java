package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm2;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm2Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm2Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm2】的数据库操作Service实现
* @createDate 2025-02-18 11:57:38
*/
@Slf4j
@Service
public class DfExcelC98bInkBm2ServiceImpl extends ServiceImpl<DfExcelC98bInkBm2Mapper, DfExcelC98bInkBm2>
    implements DfExcelC98bInkBm2Service{



    @Autowired
    private DfExcelC98bInkBm2Mapper inkBm2Mapper;





    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        List<DfExcelC98bInkBm2> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;
        int consecutiveNullCount = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheet("BM2");
            if (sheet == null) {
                throw new RuntimeException("未找到BM2工作表");
            }

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 从第7行开始读取数据
            for (int i = 6; ; i++) {
                Row row = sheet.getRow(i);

                if (row == null || isFirstColumnEmpty(row)) {
                    consecutiveNullCount++;
                    if (consecutiveNullCount >= 3) {
                        break;
                    }
                    continue;
                }

                consecutiveNullCount = 0;

                try {
                    DfExcelC98bInkBm2 data = new DfExcelC98bInkBm2();






                    // String timeStr = getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter);
                    // timeStr = timeStr.replace("T", " ");
                    //
                    //
                    // if (!timeStr.isEmpty()) {
                    //     data.setTimeVar(LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    // }



                    data.setBatchId(batchId);
                    data.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setJ3(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    data.setJ8(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    data.setJ13(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    data.setJ17(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    data.setAverage(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    data.setTestResult(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));

                    if (isValidData(data)) {
                        dataList.add(data);
                        successCount++;
                        log.debug("成功读取第{}行数据", i + 1);
                    }
                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                log.info("开始保存数据，总记录数：{}", dataList.size());
                for (DfExcelC98bInkBm2 data : dataList) {
                    try {
                        inkBm2Mapper.insert(data);
                        log.debug("成功保存数据：{}", data.getTime());
                    } catch (Exception e) {
                        log.error("保存数据失败：{}, 错误：{}", data.getTime(), e.getMessage());
                        failCount++;
                        successCount--;
                    }
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (Exception e) {
            log.error("Excel文件处理失败：{}", e.getMessage());
            result.put("success", false);
            result.put("message", "Excel文件处理失败：" + e.getMessage());
        }

        return result;
    }

    private boolean isFirstColumnEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellType() == CellType.BLANK;
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row, col)) {
                Cell firstCell = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());
                return getCellValue(firstCell, evaluator, formatter);
            }
        }
        return getCellValue(cell, evaluator, formatter);
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    }
                    return formatter.formatCellValue(cell).trim();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return formatter.formatCellValue(cell, evaluator).trim();
                case BLANK:
                    return "";
                case ERROR:
                    return "ERROR";
                default:
                    return "";
            }
        } catch (Exception e) {
            log.error("获取单元格值时发生错误：{}", e.getMessage());
            return "";
        }
    }

    private boolean isValidData(DfExcelC98bInkBm2 data) {
        return data.getTime() != null && !data.getTime().isEmpty() &&
                data.getJ3() != null && !data.getJ3().isEmpty();
    }






}




