package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwire;

import com.biel.qmsgather.domain.DfExcelC98bInkOil;
import com.biel.qmsgather.mapper.DfExcelC98bInkBm3andwireMapper;
import com.biel.qmsgather.mapper.DfExcelC98bInkOilMapper;
import com.biel.qmsgather.service.DfExcelC98bInkBm3andwireService;
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
* @description 针对表【df_excel_c98b_ink_bm3andwire】的数据库操作Service实现
* @createDate 2025-02-19 10:18:51
*/
@Service
@Slf4j
public class DfExcelC98bInkBm3andwireServiceImpl extends ServiceImpl<DfExcelC98bInkBm3andwireMapper, DfExcelC98bInkBm3andwire>
    implements DfExcelC98bInkBm3andwireService {



    @Autowired
    private DfExcelC98bInkBm3andwireMapper mapper;


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        List<DfExcelC98bInkBm3andwire> dataList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("BM1+BM2+线框");

            if (sheet == null) {
                throw new RuntimeException("未找到指定工作表: BM1+BM2+线框");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();
            int consecutiveNullCount = 0;
            int startRow = 5;

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
                    DfExcelC98bInkBm3andwire data = new DfExcelC98bInkBm3andwire();

                    // 映射数据到实体类
                    data.setTime(getCellValue(row.getCell(0), evaluator, formatter));
                    data.setJ2p(getCellValue(row.getCell(1), evaluator, formatter));
                    data.setJ5p(getCellValue(row.getCell(2), evaluator, formatter));
                    data.setJ8p(getCellValue(row.getCell(3), evaluator, formatter));
                    data.setJ19p(getCellValue(row.getCell(4), evaluator, formatter));
                    data.setJ21p(getCellValue(row.getCell(5), evaluator, formatter));
                    data.setJ17p(getCellValue(row.getCell(6), evaluator, formatter));
                    data.setAverage(getCellValue(row.getCell(7), evaluator, formatter));
                    data.setResult(getCellValue(row.getCell(8), evaluator, formatter));
                    data.setMachine(getCellValue(row.getCell(9), evaluator, formatter));
                    data.setStatus(getCellValue(row.getCell(10), evaluator, formatter));
                    data.setNoted(getCellValue(row.getCell(11), evaluator, formatter));

                    if (isValidData(data)) {
                        dataList.add(data);
                        successCount++;
                        log.debug("成功读取第{}行数据", i + 1);
                    }

                } catch (Exception e) {
                    failCount++;
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
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

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "Excel文件读取失败：" + e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bInkBm3andwire> dataList) {
        int successCount = 0;
        for (DfExcelC98bInkBm3andwire data : dataList) {
            try {
                mapper.insert(data);
                successCount++;
                log.debug("成功保存数据: {}", data.getTime());
            } catch (Exception e) {
                log.error("保存数据失败: {}, 错误: {}", data.getTime(), e.getMessage());
            }
        }
        return successCount;
    }

    private boolean isValidData(DfExcelC98bInkBm3andwire data) {
        return data.getTime() != null && !data.getTime().trim().isEmpty();
    }

    private boolean isFirstColumnEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellType() == CellType.BLANK;
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




