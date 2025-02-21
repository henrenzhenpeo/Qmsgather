package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.DfExcelC98bLayer2Program20240624Manual;
import com.biel.qmsgather.mapper.DfExcelC98bLayer2Program20240624ManualMapper;
import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualService;
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
* @description 针对表【df_excel_c98b_layer2_program_20240624_manual(C98B二层程序20240624（手动）相关数据)】的数据库操作Service实现
* @createDate 2025-02-13 11:12:33
*/

@Slf4j
@Service
public class DfExcelC98bLayer2Program20240624ManualServiceImpl extends ServiceImpl<DfExcelC98bLayer2Program20240624ManualMapper, DfExcelC98bLayer2Program20240624Manual>
    implements DfExcelC98bLayer2Program20240624ManualService{

    @Autowired
    private DfExcelC98bLayer2Program20240624ManualMapper mapper;


    @Transactional(rollbackFor = Exception.class)
    public void importExcelData(MultipartFile file, String batchId) throws Exception {
        List<DfExcelC98bLayer2Program20240624Manual> dataList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            log.info("开始读取Excel文件，总行数：{}", sheet.getLastRowNum());

            // 从第9行开始读取数据（索引从0开始，所以是8）
            for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                DfExcelC98bLayer2Program20240624Manual data = new DfExcelC98bLayer2Program20240624Manual();

                try {
                    // 读取时间
                    Cell timeCell = row.getCell(0);
                    if (timeCell != null) {
                        data.setRecordTime(timeCell.toString().trim());
                    }

                    data.setBatchId(batchId);

                    // 读取位置28-37的数值
                    data.setPosition28(getStringToBigDecimal(row.getCell(1)));
                    data.setPosition29(getStringToBigDecimal(row.getCell(2)));
                    data.setPosition30(getStringToBigDecimal(row.getCell(3)));
                    data.setPosition31(getStringToBigDecimal(row.getCell(4)));
                    data.setPosition32(getStringToBigDecimal(row.getCell(5)));
                    data.setPosition33(getStringToBigDecimal(row.getCell(6)));
                    data.setPosition34(getStringToBigDecimal(row.getCell(7)));
                    data.setPosition35(getStringToBigDecimal(row.getCell(8)));
                    data.setPosition36(getStringToBigDecimal(row.getCell(9)));
                    data.setPosition37(getStringToBigDecimal(row.getCell(10)));

                    // 读取机台号
                    Cell machineCell = row.getCell(11);
                    if (machineCell != null) {
                        data.setMachineNumber(machineCell.toString().trim());
                    }

                    // 读取状态
                    Cell statusCell = row.getCell(12);
                    if (statusCell != null) {
                        data.setStatus(statusCell.toString().trim());
                    }

                    // 读取noted
                    Cell notedCell = row.getCell(13);
                    if (notedCell != null) {
                        data.setNoted(notedCell.toString().trim());
                    }

                    if (isValidData(data)) {
                        dataList.add(data);
                        log.debug("成功读取第{}行数据: {}", i + 1, data);
                    } else {
                        log.warn("第{}行数据不完整，跳过: {}", i + 1, data);
                    }

                } catch (Exception e) {
                    log.error("读取第{}行数据失败: {}", i + 1, e.getMessage());
                    throw new RuntimeException("读取Excel数据失败：" + e.getMessage());
                }
            }

            // 批量保存数据
            if (!dataList.isEmpty()) {
                log.info("开始保存数据，总记录数：{}", dataList.size());
                for (DfExcelC98bLayer2Program20240624Manual data : dataList) {
                    mapper.insert(data);
                }
                log.info("数据保存完成");
            }
        }
    }

    private boolean isValidData(DfExcelC98bLayer2Program20240624Manual data) {
        return data.getRecordTime() != null && !data.getRecordTime().isEmpty()
                && data.getPosition28() != null
                && data.getPosition29() != null
                && data.getPosition30() != null
                && data.getPosition31() != null
                && data.getPosition32() != null
                && data.getPosition33() != null
                && data.getPosition34() != null
                && data.getPosition35() != null
                && data.getPosition36() != null
                && data.getPosition37() != null
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




