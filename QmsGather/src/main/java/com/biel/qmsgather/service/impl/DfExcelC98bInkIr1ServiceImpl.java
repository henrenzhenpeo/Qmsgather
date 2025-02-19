package com.biel.qmsgather.service.impl;

import com.biel.qmsgather.domain.DfExcelC98bInkIr1;
import com.biel.qmsgather.mapper.DfExcelC98bInkIr1Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkIr1Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkIr2Config;

import com.biel.qmsgather.mapper.DfExcelC98bInkIr2ConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bInkIr2ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir1】的数据库操作Service实现
* @createDate 2025-02-19 10:19:12
*/
@Service
@Slf4j
public class DfExcelC98bInkIr1ServiceImpl extends ServiceImpl<DfExcelC98bInkIr1Mapper, DfExcelC98bInkIr1>
    implements DfExcelC98bInkIr1Service {

    @Autowired
    private DfExcelC98bInkIr1Mapper inkIr1Mapper;


    @Transactional(rollbackFor = Exception.class)
    public int importFromExcel(MultipartFile file) {
        List<DfExcelC98bInkIr1> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);
        int startRow = 6;
        int consecutiveNullCount = 0;

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("IR1");

            if (sheet == null) {
                log.error("未找到工作表: IR1");
                throw new RuntimeException("未找到指定的工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            try {
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
                        DfExcelC98bInkIr1 data = new DfExcelC98bInkIr1();

                        data.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                        data.setRx(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                        data.setTx(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                        data.setAverage(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                        data.setTestResult(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                        data.setMachineNumber(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                        data.setStatus(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                        data.setNoted(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));

                        if (isValidData(data)) {
                            dataList.add(data);
                            log.debug("成功读取第{}行数据", i + 1);
                        } else {
                            log.warn("第{}行数据不完整，跳过", i + 1);
                        }
                    } catch (Exception e) {
                        log.error("处理第{}行数据时出错: {}", i + 1, e.getMessage());
                    }
                }
            } finally {
                workbook.close();
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSave(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkIr1> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkIr1 data : dataList) {
            try {
                inkIr1Mapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkIr1 data) {
        return data.getTime() != null && !data.getTime().isEmpty()
                && data.getRx() != null
                && data.getTx() != null
                && data.getAverage() != null
                && data.getTestResult() != null
                && data.getMachineNumber() != null;
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
            return null;
        }

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




