package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3andoil;

import com.biel.qmsgather.mapper.DfExcelC98bInkBm3andoilMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3andoilService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andoil】的数据库操作Service实现
* @createDate 2025-02-19 10:18:41
*/
@Slf4j
@Service
public class DfExcelC98bInkBm3andoilServiceImpl extends ServiceImpl<DfExcelC98bInkBm3andoilMapper, DfExcelC98bInkBm3andoil>
    implements DfExcelC98bInkBm3andoilService {




    @Autowired
    private DfExcelC98bInkBm3andoilMapper inkMapper;


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        List<DfExcelC98bInkBm3andoil> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        int successCount = 0;
        int failCount = 0;
        int consecutiveNullCount = 0;
        int startRow = 5;

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet("BM1+BM2+光油");

            if (sheet == null) {
                throw new RuntimeException("未找到指定工作表: BM1+BM2+光油");
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
                    DfExcelC98bInkBm3andoil data = new DfExcelC98bInkBm3andoil();


                    data.setBatchId(batchId);
                    // 设置数据字段
                    data.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    data.setJ2p(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    data.setJ5p(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    data.setJ8p(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    data.setJ19p(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    data.setJ21p(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    data.setJ17p(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    data.setAverage(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    data.setResult(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    data.setMachineNumber(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));
                    data.setStatus(getMergedCellValue(sheet, i, 10, row.getCell(10), evaluator, formatter));
                    data.setNoted(getMergedCellValue(sheet, i, 11, row.getCell(11), evaluator, formatter));

                    dataList.add(data);
                    successCount++;
                    log.debug("成功读取第{}行数据", i + 1);

                } catch (Exception e) {
                    failCount++;
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                try {
                    for (DfExcelC98bInkBm3andoil data : dataList) {
                        inkMapper.insert(data);
                    }
                    log.info("成功导入{}条数据到数据库", successCount);
                } catch (Exception e) {
                    log.error("保存数据失败: {}", e.getMessage());
                    throw new RuntimeException("数据保存失败", e);
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "Excel文件读取失败：" + e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return result;
    }

    // 其他辅助方法保持不变
    private boolean isFirstColumnEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellType() == CellType.BLANK;
    }

    private String getMergedCellValue(Sheet sheet, int row, int col, Cell cell,
                                      FormulaEvaluator evaluator, DataFormatter formatter) {
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




