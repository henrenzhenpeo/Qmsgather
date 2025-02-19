package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_bevel_chamfer_metrics_data】的数据库操作Service
* @createDate 2025-02-12 11:55:06
*/
public interface DfExcelC98bBevelChamferMetricsDataService extends IService<DfExcelC98bBevelChamferMetricsData> {
    public Map<String, Object> importExcelData(MultipartFile file);
}
