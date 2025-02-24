package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed;
import com.biel.qmsgather.mapper.DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedMapper;
import com.biel.qmsgather.service.DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_qr_code_position_hole_left_8x_z_newest_fixed(C98B二维码位置度（孔向左）8倍Z最新不动相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:12:46
*/
@Service
@Slf4j
public class DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedServiceImpl extends ServiceImpl<DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedMapper, DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed>
    implements DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedService{




    @Autowired
    private DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public int importExcelData(MultipartFile file,String batchId) {
        List<DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.009);

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            try {
                Sheet sheet = workbook.getSheetAt(1); // 获取第二个工作表
                log.info("开始读取Excel文件数据...");

                for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || isEmptyRow(row)) {
                        continue;
                    }

                    try {
                        DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed data = new DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed();


                        data.setBatchId(batchId);
                        // 设置各个字段的值
                        data.setRecordTime(getCellValueAsString(row.getCell(0)));

//                         String timepoint = getCellValueAsString(row.getCell(0));
//
// log.info("timepoint: {}", timepoint);;
//                         // // 将字符串时间转换为LocalDateTime
//                         data.setTimeVar(LocalDateTime.parse(timepoint, DateTimeFormatter.ofPattern("yyyy/MM/dd,HH:mm")));


                        data.setTwoDCodeHeight(getCellValueAsBigDecimal(row.getCell(1)));
                        data.setTwoDCodeWidth(getCellValueAsBigDecimal(row.getCell(2)));
                        data.setTwoDCodeCenterToC(getCellValueAsBigDecimal(row.getCell(3)));
                        data.setTwoDCodeCenterToGlassY(getCellValueAsBigDecimal(row.getCell(4)));
                        data.setTwoDCodeCenterToBm1(getCellValueAsBigDecimal(row.getCell(5)));
                        data.setAppleLogoHeight(getCellValueAsBigDecimal(row.getCell(6)));
                        data.setAppleLogoWidth(getCellValueAsBigDecimal(row.getCell(7)));
                        data.setAppleCenterToGlassX(getCellValueAsBigDecimal(row.getCell(8)));
                        data.setAppleCenterToGlassY(getCellValueAsBigDecimal(row.getCell(9)));
                        data.setLeafWidth(getCellValueAsBigDecimal(row.getCell(10)));
                        data.setLeafBottomToAppleLogoBottom(getCellValueAsBigDecimal(row.getCell(11)));
                        data.setLeafTopToAppleLogoBottom(getCellValueAsBigDecimal(row.getCell(12)));
                        data.setTwoDCodeCenterToOil(getCellValueAsBigDecimal(row.getCell(13)));
                        data.setLeafTopToC(getCellValueAsBigDecimal(row.getCell(14)));
                        data.setMachineNumber(getCellValueAsString(row.getCell(15)));
                        data.setStatus(getCellValueAsString(row.getCell(16)));

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

        } catch (Exception e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSave(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存数据，总记录数：{}", dataList.size());

        for (DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed data : dataList) {
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

    private boolean isEmptyRow(Row row) {
        if (row == null) return true;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidData(DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed data) {
        return data.getRecordTime() != null
                && data.getTwoDCodeHeight() != null
                && data.getTwoDCodeWidth() != null
                && data.getMachineNumber() != null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        return sdf.format(cell.getDateCellValue());
                    }
                    return new BigDecimal(String.valueOf(cell.getNumericCellValue()))
                            .stripTrailingZeros().toPlainString();
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("获取字符串值失败: {}", e.getMessage());
            return null;
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return new BigDecimal(String.valueOf(cell.getNumericCellValue()))
                            .setScale(6, BigDecimal.ROUND_HALF_UP);
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value)
                            .setScale(6, BigDecimal.ROUND_HALF_UP);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("转换BigDecimal失败: {}", e.getMessage());
            return null;
        }
    }















}




