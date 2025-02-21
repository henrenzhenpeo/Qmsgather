package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkDepth;

import com.biel.qmsgather.mapper.DfExcelC98bInkDepthMapper;
import com.biel.qmsgather.service.DfExcelC98bInkDepthService;
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
* @description 针对表【df_excel_c98b_ink_depth】的数据库操作Service实现
* @createDate 2025-02-19 10:19:05
*/
@Service
@Slf4j
public class DfExcelC98bInkDepthServiceImpl extends ServiceImpl<DfExcelC98bInkDepthMapper, DfExcelC98bInkDepth>
    implements DfExcelC98bInkDepthService {




    @Autowired
    private DfExcelC98bInkDepthMapper inkDepthMapper;




    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file, String batchId) {
        Map<String, Object> result = new HashMap<>();
        log.info("开始读取Excel文件: {}", file.getOriginalFilename());

        ZipSecureFile.setMinInflateRatio(0.001);
        int startRow = 6;
        int consecutiveNullCount = 0;
        List<DfExcelC98bInkDepth> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("线框厚度");

            if (sheet == null) {
                String message = "未找到工作表: 线框厚度";
                log.error(message);
                result.put("success", false);
                result.put("message", message);
                return result;
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
                    DfExcelC98bInkDepth inkDepth = new DfExcelC98bInkDepth();
                    inkDepth.setBatchId(batchId);
                    inkDepth.setTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    inkDepth.setJ2p(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    inkDepth.setJ5p(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    inkDepth.setJ8p(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    inkDepth.setJ19p(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    inkDepth.setJ21p(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    inkDepth.setJ17p(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    inkDepth.setAverage(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    inkDepth.setTestResult(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    inkDepth.setMachineNumber(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));
                    inkDepth.setStatus(getMergedCellValue(sheet, i, 10, row.getCell(10), evaluator, formatter));
                    inkDepth.setNoted(getMergedCellValue(sheet, i, 11, row.getCell(11), evaluator, formatter));

                    if (isValidData(inkDepth)) {
                        dataList.add(inkDepth);
                        log.debug("成功读取第{}行数据: {}", i + 1, inkDepth);
                    } else {
                        failCount++;
                        log.warn("第{}行数据不完整，跳过: {}", i + 1, inkDepth);
                    }
                } catch (Exception e) {
                    failCount++;
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                }
            }

            successCount = batchSave(dataList);

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "Excel文件读取失败：" + e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkDepth> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bInkDepth data : dataList) {
            try {
                inkDepthMapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkDepth data) {
        return data.getTime() != null && !data.getTime().isEmpty()
                && data.getJ2p() != null
                && data.getJ5p() != null
                && data.getJ8p() != null
                && data.getJ19p() != null
                && data.getJ21p() != null
                && data.getJ17p() != null;
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




