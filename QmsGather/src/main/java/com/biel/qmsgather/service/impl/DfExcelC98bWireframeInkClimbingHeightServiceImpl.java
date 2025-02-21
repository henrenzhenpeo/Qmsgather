package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bWireframeInkClimbingHeight;
import com.biel.qmsgather.service.DfExcelC98bWireframeInkClimbingHeightService;
import com.biel.qmsgather.mapper.DfExcelC98bWireframeInkClimbingHeightMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
* @description 针对表【df_excel_c98b_wireframe_ink_climbing_height】的数据库操作Service实现
* @createDate 2025-02-13 10:07:17
*/

@Slf4j
@Service
public class DfExcelC98bWireframeInkClimbingHeightServiceImpl extends ServiceImpl<DfExcelC98bWireframeInkClimbingHeightMapper, DfExcelC98bWireframeInkClimbingHeight>
    implements DfExcelC98bWireframeInkClimbingHeightService{



    @Autowired
    private DfExcelC98bWireframeInkClimbingHeightMapper mapper;


    @Transactional(rollbackFor = Exception.class)
    public int importFromExcel(MultipartFile file, String batchId) {
        List<DfExcelC98bWireframeInkClimbingHeight> dataList = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            try {
                Sheet sheet = workbook.getSheetAt(0);
                log.info("开始读取Excel文件，总行数：{}", sheet.getLastRowNum());

                // 从第5行开始读取数据
                for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    DfExcelC98bWireframeInkClimbingHeight data = new DfExcelC98bWireframeInkClimbingHeight();

                    try {
                        // 读取时间 - 直接获取字符串值
                        Cell timeCell = row.getCell(0);
                        if (timeCell != null) {
                            data.setRecordTime(timeCell.toString().trim());
                        }

                        // 读取各项测量值
                        data.setBatchId(batchId);
                        data.setUpperLongSideInkToGlassFrontHeight(getStringToBigDecimal(row.getCell(1)));
                        data.setLowerLongSideInkToGlassFrontHeight(getStringToBigDecimal(row.getCell(2)));
                        data.setNonGrooveShortSideInkToGlassFrontHeight(getStringToBigDecimal(row.getCell(3)));
                        data.setGrooveShortSideInkToGlassFrontHeight(getStringToBigDecimal(row.getCell(4)));
                        data.setGrooveInkToGlassBackHeight(getStringToBigDecimal(row.getCell(5)));

                        // 读取机台号
                        Cell machineCell = row.getCell(6);
                        if (machineCell != null) {
                            if (machineCell.getCellType() == CellType.STRING) {
                                String machineStr = machineCell.getStringCellValue().trim();
                                data.setMachineNumber(Integer.parseInt(machineStr));
                            } else if (machineCell.getCellType() == CellType.NUMERIC) {
                                data.setMachineNumber((int) machineCell.getNumericCellValue());
                            }
                        }

                        // 读取状态
                        Cell statusCell = row.getCell(7);
                        if (statusCell != null) {
                            data.setStatus(statusCell.getStringCellValue());
                        }

                        if (isValidData(data)) {
                            dataList.add(data);
                            log.debug("成功读取第{}行数据: {}", i + 1, data);
                        } else {
                            log.warn("第{}行数据不完整，跳过: {}", i + 1, data);
                        }

                    } catch (Exception e) {
                        log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bWireframeInkClimbingHeight> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bWireframeInkClimbingHeight data : dataList) {
            try {
                mapper.insert(data);
                successCount++;
                log.debug("成功保存记录：{}", data.getRecordTime());
            } catch (Exception e) {
                log.error("保存记录失败：{}, 错误：{}", data.getRecordTime(), e.getMessage());
            }
        }

        log.info("数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidData(DfExcelC98bWireframeInkClimbingHeight data) {
        return data.getRecordTime() != null && !data.getRecordTime().isEmpty()
                && data.getUpperLongSideInkToGlassFrontHeight() != null
                && data.getLowerLongSideInkToGlassFrontHeight() != null
                && data.getNonGrooveShortSideInkToGlassFrontHeight() != null
                && data.getGrooveShortSideInkToGlassFrontHeight() != null
                && data.getGrooveInkToGlassBackHeight() != null
                && data.getMachineNumber() != null;
    }

    private BigDecimal getStringToBigDecimal(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return new BigDecimal(value);
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            }
            return null;
        } catch (Exception e) {
            log.warn("单元格值转换为BigDecimal失败: {}", e.getMessage());
            return null;
        }
    }


















}




