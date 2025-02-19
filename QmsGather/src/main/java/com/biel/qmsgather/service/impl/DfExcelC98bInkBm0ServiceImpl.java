package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm0;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm0Mapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm0Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm0】的数据库操作Service实现
* @createDate 2025-02-18 11:57:22
*/
@Service
@Slf4j
public class DfExcelC98bInkBm0ServiceImpl extends ServiceImpl<DfExcelC98bInkBm0Mapper, DfExcelC98bInkBm0>
    implements DfExcelC98bInkBm0Service{


    @Autowired
    private DfExcelC98bInkBm0Mapper inkBm0Mapper;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<DfExcelC98bInkBm0> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        ZipSecureFile.setMinInflateRatio(0.001);

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            String sheetName = "BM0";
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("未找到工作表: " + sheetName);
            }

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            int startRow = 5;
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
                    DfExcelC98bInkBm0 data = new DfExcelC98bInkBm0();

                    data.setMeasurementTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setSd1(getNumericCellValue(row.getCell(1)));
                    data.setSd3(getNumericCellValue(row.getCell(2)));
                    data.setSd5(getNumericCellValue(row.getCell(3)));
                    data.setSd7(getNumericCellValue(row.getCell(4)));
                    data.setFd1(getNumericCellValue(row.getCell(5)));
                    data.setFd3(getNumericCellValue(row.getCell(6)));
                    data.setAverage(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    data.setTestResult(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 10, row.getCell(10), evaluator, formatter));
                    data.setNoted(getMergedCellValue(sheet, i, 11, row.getCell(11), evaluator, formatter));

                    if (isValidData(data)) {
                        dataList.add(data);
                        successCount++;
                        log.debug("成功读取第{}行数据", i + 1);
                    } else {
                        failCount++;
                        log.warn("第{}行数据不完整，已跳过", i + 1);
                    }

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时出错: {}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                successCount = batchSave(dataList);
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (Exception e) {
            log.error("Excel导入失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }

        return result;
    }

    private int batchSave(List<DfExcelC98bInkBm0> dataList) {
        int successCount = 0;
        for (DfExcelC98bInkBm0 data : dataList) {
            try {
                inkBm0Mapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getMeasurementTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getMeasurementTime(), e.getMessage());
            }
        }
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkBm0 data) {
        return data.getMeasurementTime() != null
                && data.getSd1() != null
                && data.getSd3() != null
                && data.getSd5() != null
                && data.getSd7() != null
                && data.getFd1() != null
                && data.getFd3() != null;
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

    private Double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String stringValue = cell.getStringCellValue().trim();
                return stringValue.isEmpty() ? null : Double.parseDouble(stringValue);
            }
        } catch (Exception e) {
            log.error("数值转换失败: {}", e.getMessage());
        }
        return null;
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) {
            return null;
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
                    return null;
                case ERROR:
                    return "ERROR: " + cell.getErrorCellValue();
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("获取单元格值失败: {}", e.getMessage());
            return null;
        }
    }












}




