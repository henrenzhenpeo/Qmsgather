package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkOil;

import com.biel.qmsgather.mapper.DfExcelC98bInkOilMapper;
import com.biel.qmsgather.service.DfExcelC98bInkOilService;
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
* @description 针对表【df_excel_c98b_ink_oil】的数据库操作Service实现
* @createDate 2025-02-19 10:19:24
*/
@Service
@Slf4j
public class DfExcelC98bInkOilServiceImpl extends ServiceImpl<DfExcelC98bInkOilMapper, DfExcelC98bInkOil>
    implements DfExcelC98bInkOilService {





    @Autowired
    private DfExcelC98bInkOilMapper dfExcelC98bInkOilMapper;


    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file, String batchId) {
        log.info("开始读取Excel文件: {}", file.getOriginalFilename());

        ZipSecureFile.setMinInflateRatio(0.001);
        int startRow = 6;
        int consecutiveNullCount = 0;
        List<DfExcelC98bInkOil> dataList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("光油 单层厚度 ");

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
                    DfExcelC98bInkOil inkOil = new DfExcelC98bInkOil();
                    inkOil.setBatchId(batchId);


                    // String timeStr = getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter);
                    //
                    //
                    // timeStr = timeStr.replace("T", " ");
                    //
                    //
                    // if (!timeStr.isEmpty()) {
                    //     inkOil.setTimeVar(LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    // }


                    // 设置实体属性
                    inkOil.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    inkOil.setJ3(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    inkOil.setJ8(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    inkOil.setJ13(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    inkOil.setJ17(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    inkOil.setAverage(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    inkOil.setTestResult(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    inkOil.setMachineNumber(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    inkOil.setStatus(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    inkOil.setNoted(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));

                    if (isValidData(inkOil)) {
                        dataList.add(inkOil);
                        log.debug("成功读取第{}行数据: {}", i + 1, inkOil);
                    } else {
                        log.warn("第{}行数据不完整，跳过", i + 1);
                    }

                } catch (Exception e) {
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                }
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSave(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkOil> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkOil data : dataList) {
            try {
                dfExcelC98bInkOilMapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkOil data) {
        return data.getTime() != null && !data.getTime().isEmpty()
                && data.getJ3() != null
                && data.getJ8() != null
                && data.getJ13() != null
                && data.getJ17() != null
                && data.getAverage() != null;
    }

    // 保持原有的辅助方法
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
                return "";
        }
    }









}




