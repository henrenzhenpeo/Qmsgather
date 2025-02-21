package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bUltrasonicContactAngle;
import com.biel.qmsgather.mapper.DfExcelC98bUltrasonicContactAngleMapper;
import com.biel.qmsgather.service.DfExcelC98bUltrasonicContactAngleService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ultrasonic_contact_angle】的数据库操作Service实现
* @createDate 2025-02-12 10:23:26
*/
@Service
public class DfExcelC98bUltrasonicContactAngleServiceImpl extends ServiceImpl<DfExcelC98bUltrasonicContactAngleMapper, DfExcelC98bUltrasonicContactAngle>
    implements DfExcelC98bUltrasonicContactAngleService{



    @Autowired
    private DfExcelC98bUltrasonicContactAngleMapper ultrasonicMapper;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcelFile(MultipartFile file,String batchId) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 检查文件类型
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            throw new IllegalArgumentException("只支持Excel文件格式(.xlsx或.xls)");
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            List<DfExcelC98bUltrasonicContactAngle> dataList = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;

            // 从第6行开始读取数据
            for (int rowNum = 5; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                try {
                    DfExcelC98bUltrasonicContactAngle data = new DfExcelC98bUltrasonicContactAngle();
                    data.setBatchId(batchId);

                    data.setMeasurementTime(getDateCellValue(row.getCell(0)));
                    data.setGrooveEdge(getBigDecimalCellValue(row.getCell(1)));
                    data.setGrooveRightAngle(getBigDecimalCellValue(row.getCell(2)));
                    data.setUpperLength(getBigDecimalCellValue(row.getCell(3)));
                    data.setNonGrooveRightAngle(getBigDecimalCellValue(row.getCell(4)));
                    data.setNonGrooveEdge(getBigDecimalCellValue(row.getCell(5)));
                    data.setNonGrooveLeftAngle(getBigDecimalCellValue(row.getCell(6)));
                    data.setLowerLength(getBigDecimalCellValue(row.getCell(7)));
                    data.setGrooveLeftAngle(getBigDecimalCellValue(row.getCell(8)));
                    data.setMachineNumber(getIntegerCellValue(row.getCell(9)));
                    data.setStatus(getStringCellValue(row.getCell(10)));
                    data.setNoted(getStringCellValue(row.getCell(11)));

                    ultrasonicMapper.insert(data);
                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    log.error("处理第{}行数据时发生错误：{}");
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", String.format("导入完成：成功%d条，失败%d条", successCount, failCount));

        } catch (Exception e) {
            log.error("Excel导入失败：", e);
            throw new RuntimeException("Excel导入失败：" + e.getMessage());
        }

        return result;
    }

    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < 12; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    // 其他辅助方法保持不变
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getIntegerCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            double numericValue = cell.getNumericCellValue();
            return (int) numericValue;
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal getBigDecimalCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            cell.setCellType(CellType.NUMERIC);
            double numericValue = cell.getNumericCellValue();
            return BigDecimal.valueOf(numericValue);
        } catch (Exception e) {
            return null;
        }
    }

    private Date getDateCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            return cell.getDateCellValue();
        } catch (Exception e) {
            try {
                double numericValue = cell.getNumericCellValue();
                return DateUtil.getJavaDate(numericValue);
            } catch (Exception ex) {
                return null;
            }
        }
    }

















}




