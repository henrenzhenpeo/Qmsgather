package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bWireframeInkClimbingHeightConfig;
import com.biel.qmsgather.service.DfExcelC98bWireframeInkClimbingHeightConfigService;
import com.biel.qmsgather.mapper.DfExcelC98bWireframeInkClimbingHeightConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_ink_climbing_height_config】的数据库操作Service实现
* @createDate 2025-02-13 10:07:21
*/

@Slf4j
@Service
public class DfExcelC98bWireframeInkClimbingHeightConfigServiceImpl extends ServiceImpl<DfExcelC98bWireframeInkClimbingHeightConfigMapper, DfExcelC98bWireframeInkClimbingHeightConfig>
    implements DfExcelC98bWireframeInkClimbingHeightConfigService{



    @Autowired
    private DfExcelC98bWireframeInkClimbingHeightConfigMapper configMapper;

    @Override
    public int importConfigFromExcel(MultipartFile file) {
        List<DfExcelC98bWireframeInkClimbingHeightConfig> dataList = new ArrayList<>();
        ZipSecureFile.setMinInflateRatio(0.001);

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            try {
                Sheet sheet = workbook.getSheetAt(0);
                log.info("开始读取Excel配置数据，处理第3-4行");

                // 遍历第三行到第四行
                for (int rowIndex = 2; rowIndex <= 3; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    DfExcelC98bWireframeInkClimbingHeightConfig config = new DfExcelC98bWireframeInkClimbingHeightConfig();

                    try {
                        // 设置各个字段的值
                        config.setStandardType(getCellValueAsString(row.getCell(0)));
                        config.setUpperLongSideInkToGlassFrontHeight(getCellValueAsString(row.getCell(1)));
                        config.setLowerLongSideInkToGlassFrontHeight(getCellValueAsString(row.getCell(2)));
                        config.setNonGrooveShortSideInkToGlassFrontHeight(getCellValueAsString(row.getCell(3)));
                        config.setGrooveShortSideInkToGlassFrontHeight(getCellValueAsString(row.getCell(4)));
                        config.setGrooveInkToGlassBackHeight(getCellValueAsString(row.getCell(5)));

                        if (isValidConfigData(config)) {
                            dataList.add(config);
                            log.debug("成功读取第{}行配置数据: {}", rowIndex + 1, config);
                        } else {
                            log.warn("第{}行配置数据不完整，跳过: {}", rowIndex + 1, config);
                        }

                    } catch (Exception e) {
                        log.error("读取第{}行配置数据失败: {}", rowIndex + 1, e.getMessage());
                    }
                }

            } finally {
                workbook.close();
            }

        } catch (IOException e) {
            log.error("Excel文件读取失败: {}", e.getMessage());
            throw new RuntimeException("Excel文件读取失败", e);
        }

        return batchSaveConfig(dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchSaveConfig(List<DfExcelC98bWireframeInkClimbingHeightConfig> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        log.info("开始保存配置数据，总记录数：{}", dataList.size());

        for (DfExcelC98bWireframeInkClimbingHeightConfig data : dataList) {
            try {
                configMapper.insert(data);
                successCount++;

                // 打印保存的数据
                log.info("==================== 保存配置数据 ====================");
                log.info("类型: {}", data.getStandardType());
                log.info("上长边企身油墨到玻璃正面高度: {}", data.getUpperLongSideInkToGlassFrontHeight());
                log.info("下长边企身油墨到玻璃正面高度: {}", data.getLowerLongSideInkToGlassFrontHeight());
                log.info("非凹槽短边企身油墨到玻璃正面高度: {}", data.getNonGrooveShortSideInkToGlassFrontHeight());
                log.info("凹槽短边企身油墨到玻璃正面高度: {}", data.getGrooveShortSideInkToGlassFrontHeight());
                log.info("凹槽企身油墨到玻璃背面高度: {}", data.getGrooveInkToGlassBackHeight());
                log.info("================================================");

            } catch (Exception e) {
                log.error("保存配置数据失败：{}, 错误：{}", data.getStandardType(), e.getMessage());
            }
        }

        log.info("配置数据保存完成，成功保存{}条记录", successCount);
        return successCount;
    }

    private boolean isValidConfigData(DfExcelC98bWireframeInkClimbingHeightConfig data) {
        return data.getStandardType() != null && !data.getStandardType().isEmpty()
                && data.getUpperLongSideInkToGlassFrontHeight() != null
                && data.getLowerLongSideInkToGlassFrontHeight() != null
                && data.getNonGrooveShortSideInkToGlassFrontHeight() != null
                && data.getGrooveShortSideInkToGlassFrontHeight() != null
                && data.getGrooveInkToGlassBackHeight() != null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }






}




