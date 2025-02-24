package com.biel.qmsgather.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624Manual;
import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624ManualConfig;
import com.biel.qmsgather.mapper.DfExcelC98bWireframe20240624ManualConfigMapper;
import com.biel.qmsgather.mapper.DfExcelC98bWireframe20240624ManualMapper;
import com.biel.qmsgather.service.DfExcelC98bWireframe20240624ManualService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_20240624_manual(C98B线框20240624（手动）相关数据)】的数据库操作Service实现
* @createDate 2025-02-20 15:34:42
*/
@Service
@Slf4j
public class DfExcelC98bWireframe20240624ManualServiceImpl extends ServiceImpl<DfExcelC98bWireframe20240624ManualMapper, DfExcelC98bWireframe20240624Manual>
    implements DfExcelC98bWireframe20240624ManualService {


    @Autowired
    private DfExcelC98bWireframe20240624ManualMapper mapper;


    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file,String batchId) {
        List<DfExcelC98bWireframe20240624Manual> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);
        int successCount = 0;
        int consecutiveNullCount = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("线框");

            if (sheet == null) {
                log.error("未找到工作表: 线框");
                throw new RuntimeException("未找到指定的工作表");
            }

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // 从第10行开始读取数据(索引从0开始，所以是9)
            for (int i = 9; ; i++) {
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
                    DfExcelC98bWireframe20240624Manual entity = new DfExcelC98bWireframe20240624Manual();


                    entity.setBatchId(batchId);
                    // 设置所有字段值


                    // String timeStr = getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter);
                    // if (!timeStr.isEmpty()) {
                    //     entity.setTimeVar(LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy/MM/dd,HH:mm")));
                    // }




                    entity.setRecordTime(getMergedCellValue(sheet, i, 0, row.getCell(0), evaluator, formatter));
                    entity.setY1(getMergedCellValue(sheet, i, 1, row.getCell(1), evaluator, formatter));
                    entity.setY1XF(getMergedCellValue(sheet, i, 2, row.getCell(2), evaluator, formatter));
                    entity.setY2(getMergedCellValue(sheet, i, 3, row.getCell(3), evaluator, formatter));
                    entity.setXdz3(getMergedCellValue(sheet, i, 4, row.getCell(4), evaluator, formatter));
                    entity.setXdz4(getMergedCellValue(sheet, i, 5, row.getCell(5), evaluator, formatter));
                    entity.setY25y2(getMergedCellValue(sheet, i, 6, row.getCell(6), evaluator, formatter));
                    entity.setY284(getMergedCellValue(sheet, i, 7, row.getCell(7), evaluator, formatter));
                    entity.setY26y1(getMergedCellValue(sheet, i, 8, row.getCell(8), evaluator, formatter));
                    entity.setX7(getMergedCellValue(sheet, i, 9, row.getCell(9), evaluator, formatter));
                    entity.setX8(getMergedCellValue(sheet, i, 10, row.getCell(10), evaluator, formatter));
                    entity.setR1(getMergedCellValue(sheet, i, 11, row.getCell(11), evaluator, formatter));
                    entity.setR2(getMergedCellValue(sheet, i, 12, row.getCell(12), evaluator, formatter));
                    entity.setR3(getMergedCellValue(sheet, i, 13, row.getCell(13), evaluator, formatter));
                    entity.setR4(getMergedCellValue(sheet, i, 14, row.getCell(14), evaluator, formatter));
                    entity.setShapeLength1(getMergedCellValue(sheet, i, 15, row.getCell(15), evaluator, formatter));
                    entity.setShapeLength2(getMergedCellValue(sheet, i, 16, row.getCell(16), evaluator, formatter));
                    entity.setOutlineWidth94(getMergedCellValue(sheet, i, 17, row.getCell(17), evaluator, formatter));
                    entity.setOutlineWidth95(getMergedCellValue(sheet, i, 18, row.getCell(18), evaluator, formatter));
                    entity.setOutlineWidth96(getMergedCellValue(sheet, i, 19, row.getCell(19), evaluator, formatter));
                    entity.setGrooveWidth97(getMergedCellValue(sheet, i, 20, row.getCell(20), evaluator, formatter));
                    entity.setGrooveWidth98(getMergedCellValue(sheet, i, 21, row.getCell(21), evaluator, formatter));
                    entity.setGrooveWidth99(getMergedCellValue(sheet, i, 22, row.getCell(22), evaluator, formatter));
                    entity.setGrooveWidth100(getMergedCellValue(sheet, i, 23, row.getCell(23), evaluator, formatter));
                    entity.setGrooveWidth101(getMergedCellValue(sheet, i, 24, row.getCell(24), evaluator, formatter));
                    entity.setGrooveLength102(getMergedCellValue(sheet, i, 25, row.getCell(25), evaluator, formatter));
                    entity.setAllBodyInk103(getMergedCellValue(sheet, i, 26, row.getCell(26), evaluator, formatter));
                    entity.setLengthDifference(getMergedCellValue(sheet, i, 27, row.getCell(27), evaluator, formatter));
                    entity.setWidthDifference(getMergedCellValue(sheet, i, 28, row.getCell(28), evaluator, formatter));
                    entity.setGrooveWidthDifference(getMergedCellValue(sheet, i, 29, row.getCell(29), evaluator, formatter));
                    entity.setMachineNumber(getMergedCellValue(sheet, i, 30, row.getCell(30), evaluator, formatter));
                    entity.setStatus("1"); // 默认状态
                    entity.setNoted(getMergedCellValue(sheet, i, 31, row.getCell(31), evaluator, formatter));

                    if (isValidData(entity)) {
                        mapper.insert(entity);
                        successCount++;
                        log.debug("成功读取并保存第{}行数据", i + 1);
                    } else {
                        log.warn("第{}行数据不完整，跳过", i + 1);
                    }

                } catch (Exception e) {
                    log.error("处理第{}行数据时发生错误：{}", i + 1, e.getMessage());
                }
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        log.info("数据导入完成，成功导入{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bWireframe20240624Manual data) {
        return data.getRecordTime() != null && !data.getRecordTime().isEmpty()
                && data.getMachineNumber() != null && !data.getMachineNumber().isEmpty();
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

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    // 使用DataFormatter来确保数字格式的一致性
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
                return "";
            case ERROR:
                return "";
            default:
                return "";
        }
    }





    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cell.getDateCellValue());
                    }
                    return BigDecimal.valueOf(cell.getNumericCellValue())
                            .stripTrailingZeros().toPlainString();
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("获取单元格值失败：{}", e.getMessage());
            return null;
        }
    }




}




