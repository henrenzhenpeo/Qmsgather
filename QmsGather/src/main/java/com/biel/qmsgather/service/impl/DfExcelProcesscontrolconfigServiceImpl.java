package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelProcesscontrolconfig;
import com.biel.qmsgather.mapper.DfExcelProcesscontrolconfigMapper;
import com.biel.qmsgather.service.DfExcelProcesscontrolconfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_processcontrolconfig】的数据库操作Service实现
* @createDate 2025-02-12 09:43:57
*/
@Slf4j
@Service
public class DfExcelProcesscontrolconfigServiceImpl extends ServiceImpl<DfExcelProcesscontrolconfigMapper, DfExcelProcesscontrolconfig>
    implements DfExcelProcesscontrolconfigService{


    @Autowired
    private DfExcelProcesscontrolconfigMapper configMapper;

    public Map<String, Object> importExcelData(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        try {
            ZipSecureFile.setMinInflateRatio(0.001);
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);

            log.info("开始读取Excel文件：{}", file.getOriginalFilename());

            // 定义三种类型的配置
            String[] configTypes = {"标准尺寸", "公差上限", "公差下限"};

            // 读取第3-5行数据（索引从0开始，所以是2-4）
            for (int rowIndex = 2; rowIndex <= 4; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                try {
                    DfExcelProcesscontrolconfig config = new DfExcelProcesscontrolconfig();

                    // 设置类型
                    config.setPunctuationsize(configTypes[rowIndex - 2]);

                    // 按顺序设置各个字段的值
                    config.setOuterlengthupper(getNumericCellValue(row.getCell(1), evaluator));
                    config.setOuterlengthlower(getNumericCellValue(row.getCell(2), evaluator));
                    config.setOuterwidthupper(getNumericCellValue(row.getCell(3), evaluator));
                    config.setOuterwidthlower(getNumericCellValue(row.getCell(4), evaluator));
                    config.setInlayheight(getNumericCellValue(row.getCell(5), evaluator));
                    config.setInlaywidth(getNumericCellValue(row.getCell(6), evaluator));
                    config.setInlaybottomtoglassbottom(getNumericCellValue(row.getCell(7), evaluator));
                    config.setInlaytoglasscenter(getNumericCellValue(row.getCell(8), evaluator));
                    config.setInlaytoptoglasstop(getNumericCellValue(row.getCell(9), evaluator));
                    config.setInlaybottomtoglassbottomdistance(getNumericCellValue(row.getCell(10), evaluator));
                    config.setMachinenumber(getStringCellValue(row.getCell(11), evaluator));
                    config.setNotes(getStringCellValue(row.getCell(12), evaluator));
                    config.setWorkstation(getStringCellValue(row.getCell(13), evaluator));

                    // 保存到数据库
                    configMapper.insert(config);
                    successCount++;
                    log.info("成功保存{}配置数据: {}", configTypes[rowIndex - 2], config);

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}", rowIndex + 1, e.getMessage());
                }
            }

            workbook.close();

        } catch (IOException e) {
            log.error("读取Excel文件失败：", e);
            result.put("success", false);
            result.put("message", "读取Excel文件失败：" + e.getMessage());
            return result;
        }

        result.put("success", true);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    private BigDecimal getNumericCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        try {
            CellValue cellValue;
            switch (cell.getCellType()) {
                case FORMULA:
                    cellValue = evaluator.evaluate(cell);
                    return BigDecimal.valueOf(cellValue.getNumberValue());
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String stringValue = cell.getStringCellValue().trim();
                    return stringValue.isEmpty() ? null : new BigDecimal(stringValue);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("单元格[{}]转换BigDecimal失败：{}", cell.getAddress(), e.getMessage());
            return null;
        }
    }

    private String getStringCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        try {
            CellValue cellValue;
            switch (cell.getCellType()) {
                case FORMULA:
                    cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case NUMERIC:
                            return String.valueOf(cellValue.getNumberValue());
                        case STRING:
                            return cellValue.getStringValue();
                        default:
                            return "";
                    }
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                default:
                    return "";
            }
        } catch (Exception e) {
            log.error("单元格[{}]转换String失败：{}", cell.getAddress(), e.getMessage());
            return "";
        }
    }



















}




