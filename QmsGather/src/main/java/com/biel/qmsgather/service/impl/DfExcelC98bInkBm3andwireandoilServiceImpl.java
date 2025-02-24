package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwireandoil;

import com.biel.qmsgather.mapper.DfExcelC98bInkBm3andwireandoilMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3andwireandoilService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andwireandoil】的数据库操作Service实现
* @createDate 2025-02-19 10:18:57
*/
@Service
@Slf4j
public class DfExcelC98bInkBm3andwireandoilServiceImpl extends ServiceImpl<DfExcelC98bInkBm3andwireandoilMapper, DfExcelC98bInkBm3andwireandoil>
    implements DfExcelC98bInkBm3andwireandoilService {

    @Autowired
    private DfExcelC98bInkBm3andwireandoilMapper mapper;



    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file, String batchId) {
        log.info("开始读取Excel文件...");
        List<DfExcelC98bInkBm3andwireandoil> dataList = new ArrayList<>();
        int successCount = 0;

        ZipSecureFile.setMinInflateRatio(0.001);
        int startRow = 5;
        int consecutiveNullCount = 0;

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("BM1+BM2+线框+光油");

            if (sheet == null) {
                log.error("未找到指定工作表");
                throw new RuntimeException("未找到指定工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            for (int i = startRow; ; i++) {
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
                    DfExcelC98bInkBm3andwireandoil data = new DfExcelC98bInkBm3andwireandoil();
                    data.setBatchId(batchId);

                    //
                    // String timeStr = getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter);
                    // timeStr = timeStr.replace("T", " ");
                    //
                    //
                    // if (!timeStr.isEmpty()) {
                    //     data.setTimeVar(LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    // }


                    // 设置数据字段
                    data.setRecordTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setJ2p(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    data.setJ5p(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    data.setJ8p(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    data.setJ19p(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    data.setJ21p(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    data.setJ17p(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    data.setJ3(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    data.setJ8(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    data.setJ13(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));
                    data.setJ17(getMergedCellValue(sheet, i, 10, row.getCell(10), evaluator, formatter));
                    data.setAverage(getMergedCellValue(sheet, i, 11, row.getCell(11), evaluator, formatter));
                    data.setTestResult(getMergedCellValue(sheet, i, 12, row.getCell(12), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 13, row.getCell(13), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 14, row.getCell(14), evaluator, formatter));
                    data.setNoted(getMergedCellValue(sheet, i, 15, row.getCell(15), evaluator, formatter));

                    if (isValidData(data)) {
                        dataList.add(data);
                        log.debug("成功读取第{}行数据", i + 1);
                    }
                } catch (Exception e) {
                    log.error("读取第{}行数据时发生错误: {}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                successCount = batchSave(dataList);
            }

            workbook.close();

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return successCount;
    }

    private int batchSave(List<DfExcelC98bInkBm3andwireandoil> dataList) {
        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkBm3andwireandoil data : dataList) {
            try {
                mapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getRecordTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getRecordTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }



    private boolean isFirstColumnEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellType() == CellType.BLANK;
    }

    private boolean isValidData(DfExcelC98bInkBm3andwireandoil data) {
        return data.getRecordTime() != null && !data.getRecordTime().trim().isEmpty();
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
            return "null";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return formatter.formatCellValue(cell);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return formatter.formatCellValue(cell, evaluator);
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return "";
            case ERROR:
                return "ERROR: " + cell.getErrorCellValue();
            default:
                return "unknown";
        }
    }





}




