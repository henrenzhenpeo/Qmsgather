package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig;
import com.biel.qmsgather.mapper.DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigMapper;
import com.biel.qmsgather.service.DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
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
* @description 针对表【df_excel_c98b_qr_code_position_hole_left_8x_z_newest_config(C98B二维码位置度（孔向左）8倍Z最新不动相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:12:44
*/
@Service
@Slf4j
public class DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigServiceImpl extends ServiceImpl<DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigMapper, DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig>
    implements DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigService{




    @Autowired
    private DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigMapper mapper;






    @Transactional(rollbackFor = Exception.class)
    public int importConfigFromExcel(MultipartFile file,String batchId) {
        List<DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig> configList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            // 尝试获取名为"C98B"的工作表
            Sheet sheet = workbook.getSheet("C98B");
            if (sheet == null) {
                log.warn("未找到名为'C98B'的工作表，尝试使用第二个工作表");
                // 如果找不到指定名称的工作表，使用第二个工作表（索引为1）
                sheet = workbook.getSheetAt(1);
            }

            if (sheet == null) {
                log.error("无法找到有效的工作表");
                return 0;
            }

            // 配置要读取的行范围（第3行到第7行）
            int startRow = 2;
            int endRow = 6;

            log.info("开始读取Excel配置数据，处理第3-7行，当前工作表名称：{}", sheet.getSheetName());

            // 遍历指定行
            for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    log.warn("第{}行为空，跳过", rowNum + 1);
                    continue;
                }

                try {
                    DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig config = new DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig();


                    config.setBatchId(batchId);
                    // 设置各个字段的值
                    config.setRecordTime(getStringValue(row.getCell(0)));
                    config.setTwoDCodeHeight(getBigDecimalValue(row.getCell(1)));
                    config.setTwoDCodeWidth(getBigDecimalValue(row.getCell(2)));
                    config.setTwoDCodeCenterToC(getBigDecimalValue(row.getCell(3)));
                    config.setTwoDCodeCenterToGlassY(getBigDecimalValue(row.getCell(4)));
                    config.setTwoDCodeCenterToBm1(getBigDecimalValue(row.getCell(5)));
                    config.setAppleLogoHeight(getBigDecimalValue(row.getCell(6)));
                    config.setAppleLogoWidth(getBigDecimalValue(row.getCell(7)));
                    config.setAppleCenterToGlassX(getBigDecimalValue(row.getCell(8)));
                    config.setAppleCenterToGlassY(getBigDecimalValue(row.getCell(9)));
                    config.setLeafWidth(getBigDecimalValue(row.getCell(10)));
                    config.setLeafBottomToAppleLogoBottom(getBigDecimalValue(row.getCell(11)));
                    config.setLeafTopToAppleLogoBottom(getBigDecimalValue(row.getCell(12)));
                    config.setTwoDCodeCenterToOil(getBigDecimalValue(row.getCell(13)));
                    config.setLeafTopToC(getBigDecimalValue(row.getCell(14)));

                    if (isValidConfigData(config)) {
                        configList.add(config);
                        log.debug("成功读取第{}行配置数据: {}", rowNum + 1, config);
                    } else {
                        log.warn("第{}行配置数据不完整，跳过: {}", rowNum + 1, config);
                    }

                } catch (Exception e) {
                    log.error("读取第{}行配置数据失败: {}", rowNum + 1, e.getMessage());
                }
            }

            workbook.close();

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(configList);
    }

    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public int importConfigFromExcel(MultipartFile file) {
    //     List<DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig> configList = new ArrayList<>();
    //     ZipSecureFile.setMinInflateRatio(0.001);
    //
    //     try {
    //         Workbook workbook = WorkbookFactory.create(file.getInputStream());
    //         Sheet sheet = workbook.getSheetAt(0);
    //
    //         // 配置要读取的行范围（第3行到第7行）
    //         int startRow = 2;
    //         int endRow = 6;
    //
    //         log.info("开始读取Excel配置数据，处理第3-7行");
    //
    //         // 遍历指定行
    //         for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
    //             Row row = sheet.getRow(rowNum);
    //             if (row == null) {
    //                 log.warn("第{}行为空，跳过", rowNum + 1);
    //                 continue;
    //             }
    //
    //             try {
    //                 DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig config = new DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig();
    //
    //                 // 设置各个字段的值
    //                 config.setRecordTime(getStringValue(row.getCell(0)));
    //                 config.setTwoDCodeHeight(getBigDecimalValue(row.getCell(1)));
    //                 config.setTwoDCodeWidth(getBigDecimalValue(row.getCell(2)));
    //                 config.setTwoDCodeCenterToC(getBigDecimalValue(row.getCell(3)));
    //                 config.setTwoDCodeCenterToGlassY(getBigDecimalValue(row.getCell(4)));
    //                 config.setTwoDCodeCenterToBm1(getBigDecimalValue(row.getCell(5)));
    //                 config.setAppleLogoHeight(getBigDecimalValue(row.getCell(6)));
    //                 config.setAppleLogoWidth(getBigDecimalValue(row.getCell(7)));
    //                 config.setAppleCenterToGlassX(getBigDecimalValue(row.getCell(8)));
    //                 config.setAppleCenterToGlassY(getBigDecimalValue(row.getCell(9)));
    //                 config.setLeafWidth(getBigDecimalValue(row.getCell(10)));
    //                 config.setLeafBottomToAppleLogoBottom(getBigDecimalValue(row.getCell(11)));
    //                 config.setLeafTopToAppleLogoBottom(getBigDecimalValue(row.getCell(12)));
    //                 config.setTwoDCodeCenterToOil(getBigDecimalValue(row.getCell(13)));
    //                 config.setLeafTopToC(getBigDecimalValue(row.getCell(14)));
    //
    //                 if (isValidConfigData(config)) {
    //                     configList.add(config);
    //                     log.debug("成功读取第{}行配置数据: {}", rowNum + 1, config);
    //                 } else {
    //                     log.warn("第{}行配置数据不完整，跳过: {}", rowNum + 1, config);
    //                 }
    //
    //             } catch (Exception e) {
    //                 log.error("读取第{}行配置数据失败: {}", rowNum + 1, e.getMessage());
    //             }
    //         }
    //
    //         workbook.close();
    //
    //     } catch (IOException e) {
    //         log.error("Excel文件读取失败: {}", e.getMessage());
    //         throw new RuntimeException("Excel文件读取失败", e);
    //     }
    //
    //     return batchSaveConfig(configList);
    // }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig> configList) {
        if (configList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", configList.size());

        for (DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig config : configList) {
            try {
                mapper.insert(config);
                successCount++;
                log.debug("成功保存配置数据：{}", config.getRecordTime());
            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", config.getRecordTime(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig config) {
        return config.getRecordTime() != null
                && config.getTwoDCodeHeight() != null
                && config.getTwoDCodeWidth() != null
                && config.getTwoDCodeCenterToC() != null
                && config.getTwoDCodeCenterToGlassY() != null;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return null;
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private BigDecimal getBigDecimalValue(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return new BigDecimal(String.valueOf(cell.getNumericCellValue()));
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value);
                case FORMULA:
                    return new BigDecimal(String.valueOf(cell.getNumericCellValue()));
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("转换单元格值到BigDecimal时发生错误: {}", e.getMessage());
            return null;
        }
    }

}




