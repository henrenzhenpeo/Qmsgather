package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bBevelChamferMetricsConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_bevel_chamfer_metrics_config】的数据库操作Service
* @createDate 2025-02-12 11:55:09
*/
public interface DfExcelC98bBevelChamferMetricsConfigService extends IService<DfExcelC98bBevelChamferMetricsConfig> {


    public int importConfigFromExcel(MultipartFile file);

}
