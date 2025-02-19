package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkIr2;

import com.biel.qmsgather.mapper.DfExcelC98bInkIr2Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkIr2Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir2】的数据库操作Service实现
* @createDate 2025-02-19 10:19:18
*/
@Service
@Slf4j
public class DfExcelC98bInkIr2ServiceImpl extends ServiceImpl<DfExcelC98bInkIr2Mapper, DfExcelC98bInkIr2>
    implements DfExcelC98bInkIr2Service {






    @Autowired
    private DfExcelC98bInkIr2Mapper inkIr2Mapper;


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        log.info("开始读取Excel文件...");

        ZipSecureFile.setMinInflateRatio(0.001);
        int startRow = 6;
        int consecutiveNullCount = 0;
        List<DfExcelC98bInkIr2> dataList = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("IR2");

            if (sheet == null) {
                log.error("未找到工作表: IR2");
                result.put("success", false);
                result.put("message", "未找到IR2工作表");
                return result;
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            for (int i = startRow; ; i++) {
                Row row = sheet.getRow(i);

                if (row == null || isFirstColumnEmpty(row)) {
                    consecutiveNullCount++;
                    if (consecutiveNullCount >= 3) {
                        log.info("检测到3个连续空行,停止读取");
                        break;
                    }
                    continue;
                }

                consecutiveNullCount = 0;

                try {
                    DfExcelC98bInkIr2 data = new DfExcelC98bInkIr2();

                    data.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setRx(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    data.setTx(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    data.setAverage(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    data.setTestResult(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    data.setNoted(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));

                    dataList.add(data);
                    log.info("成功读取第{}行数据", i + 1);
                } catch (Exception e) {
                    log.error("处理第{}行数据时出错: {}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            int successCount = batchSaveData(dataList);

            result.put("success", true);
            result.put("message", "导入成功");
            result.put("totalCount", dataList.size());
            result.put("successCount", successCount);

            workbook.close();

        } catch (IOException e) {
            log.error("读取Excel文件出错: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "文件处理失败：" + e.getMessage());
        }

        log.info("Excel文件读取完成");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveData(List<DfExcelC98bInkIr2> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkIr2 data : dataList) {
            try {
                inkIr2Mapper.insert(data);
                successCount++;
            } catch (Exception e) {
                log.error("保存数据失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    // 其他辅助方法保持不变
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
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return formatter.formatCellValue(cell).trim();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return formatter.formatCellValue(cell, evaluator).trim();
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return null;
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return null;
        }
    }









}




