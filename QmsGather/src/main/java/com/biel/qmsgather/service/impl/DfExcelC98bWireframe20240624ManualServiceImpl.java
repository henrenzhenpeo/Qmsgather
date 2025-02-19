package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bRCornerGroove;
import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624Manual;
import com.biel.qmsgather.mapper.DfExcelC98bRCornerGrooveMapper;
import com.biel.qmsgather.mapper.DfExcelC98bWireframe20240624ManualMapper;
import com.biel.qmsgather.service.DfExcelC98bWireframe20240624ManualService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_20240624_manual(C98B线框20240624（手动）相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:13:11
*/
@Slf4j
@Service
public class DfExcelC98bWireframe20240624ManualServiceImpl extends ServiceImpl<DfExcelC98bWireframe20240624ManualMapper, DfExcelC98bWireframe20240624Manual>
    implements DfExcelC98bWireframe20240624ManualService{



    @Autowired
    private DfExcelC98bRCornerGrooveMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file) {
        List<DfExcelC98bRCornerGroove> dataList = new ArrayList<>();
        int successCount = 0;

        try {
            log.info("开始读取Excel文件: {}", file.getOriginalFilename());
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);
            log.info("成功打开工作表，总行数：{}", sheet.getLastRowNum());

            // 从第9行开始读取数据
            for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isEmptyRow(row)) {
                    log.debug("第{}行为空行，跳过", i + 1);
                    continue;
                }

                try {
                    DfExcelC98bRCornerGroove data = readRowData(row);
                    dataList.add(data);
                    log.debug("第{}行数据读取成功", i + 1);
                } catch (Exception e) {
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                }
            }

            // 批量插入数据
            if (!dataList.isEmpty()) {
                log.info("开始插入数据到数据库，总记录数：{}", dataList.size());
                for (DfExcelC98bRCornerGroove data : dataList) {
                    try {
                        mapper.insert(data);
                        successCount++;
                    } catch (Exception e) {
                        log.error("插入记录失败：{}, 错误：{}", data.getRecordTime(), e.getMessage());
                    }
                }
            }

            workbook.close();
            log.info("Excel文件处理完成，成功导入{}条记录", successCount);

        } catch (Exception e) {
            log.error("处理Excel文件失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件处理失败: " + e.getMessage());
        }

        return successCount;
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) return true;

        for (int i = 0; i < 18; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private DfExcelC98bRCornerGroove readRowData(Row row) {
        DfExcelC98bRCornerGroove data = new DfExcelC98bRCornerGroove();

        data.setRecordTime(getCellValueAsString(row.getCell(0)));
        data.setR8(getCellValueAsBigDecimal(row.getCell(1)));
        data.setR1(getCellValueAsBigDecimal(row.getCell(2)));
        data.setR2(getCellValueAsBigDecimal(row.getCell(3)));
        data.setR3(getCellValueAsBigDecimal(row.getCell(4)));
        data.setR4(getCellValueAsBigDecimal(row.getCell(5)));
        data.setRightOutside(getCellValueAsBigDecimal(row.getCell(6)));
        data.setRightInside(getCellValueAsBigDecimal(row.getCell(7)));
        data.setLeftInside(getCellValueAsBigDecimal(row.getCell(8)));
        data.setLeftOutside(getCellValueAsBigDecimal(row.getCell(9)));
        data.setGroove1(getCellValueAsBigDecimal(row.getCell(10)));
        data.setGroove2(getCellValueAsBigDecimal(row.getCell(11)));
        data.setGroove3(getCellValueAsBigDecimal(row.getCell(12)));
        data.setGroove4(getCellValueAsBigDecimal(row.getCell(13)));
        data.setGroove5(getCellValueAsBigDecimal(row.getCell(14)));

        Cell machineCell = row.getCell(15);
        if (machineCell != null) {
            if (machineCell.getCellType() == CellType.NUMERIC) {
                data.setMachineNumber((int) machineCell.getNumericCellValue());
            } else {
                String value = machineCell.getStringCellValue().trim();
                if (!value.isEmpty()) {
                    data.setMachineNumber(Integer.parseInt(value));
                }
            }
        }

        data.setStatus(getCellValueAsString(row.getCell(16)));
        data.setNoted(getCellValueAsString(row.getCell(17)));

        return data;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("转换单元格值到BigDecimal失败: {}", e.getMessage());
            return null;
        }
    }


}




