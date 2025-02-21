package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm3Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3】的数据库操作Service实现
* @createDate 2025-02-18 11:57:44
*/
@Slf4j
@Service
public class DfExcelC98bInkBm3ServiceImpl extends ServiceImpl<DfExcelC98bInkBm3Mapper, DfExcelC98bInkBm3>
    implements DfExcelC98bInkBm3Service{





    @Autowired
    private DfExcelC98bInkBm3Mapper inkBm3Mapper;



    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file, String batchId) {
        List<DfExcelC98bInkBm3> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheet("BM1+BM2");

            if (sheet == null) {
                log.error("未找到工作表: BM1+BM2");
                throw new RuntimeException("未找到指定的工作表");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();
            int startRow = 6;
            int consecutiveNullCount = 0;

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
                    DfExcelC98bInkBm3 data = new DfExcelC98bInkBm3();
                    data.setBatchId(batchId);

                    // 读取每列数据并设置到实体类中
                    data.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setJ3(getBigDecimalValue(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter)));
                    data.setJ8(getBigDecimalValue(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter)));
                    data.setJ13(getBigDecimalValue(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter)));
                    data.setJ17(getBigDecimalValue(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter)));
                    data.setAverage(getBigDecimalValue(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter)));
                    data.setTestResult(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    data.setMachineNumber(getIntegerValue(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter)));
                    data.setStatus(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    data.setNoted(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));

                    if (isValidData(data)) {
                        dataList.add(data);
                        log.debug("成功读取第{}行数据", i + 1);
                    }
                } catch (Exception e) {
                    log.error("处理第{}行数据时出错: {}", i + 1, e.getMessage());
                }
            }

            workbook.close();

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSave(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkBm3> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkBm3 data : dataList) {
            try {
                inkBm3Mapper.insert(data);
                successCount++;
                log.debug("成功保存数据: {}", data.getTime());
            } catch (Exception e) {
                log.error("保存数据失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    // 其他辅助方法保持不变
    private boolean isValidData(DfExcelC98bInkBm3 data) {
        return data.getTime() != null && !data.getTime().trim().isEmpty();
    }

    private BigDecimal getBigDecimalValue(String value) {
        try {
            if (value != null && !value.trim().isEmpty() && !value.equals("null")) {
                return new BigDecimal(value.trim());
            }
        } catch (NumberFormatException e) {
            log.warn("转换BigDecimal失败: {}", value);
        }
        return null;
    }

    private Integer getIntegerValue(String value) {
        try {
            if (value != null && !value.trim().isEmpty() && !value.equals("null")) {
                return Integer.parseInt(value.trim());
            }
        } catch (NumberFormatException e) {
            log.warn("转换Integer失败: {}", value);
        }
        return null;
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
                return null;
        }
    }

}




