package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkOilConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_oil_config】的数据库操作Service
* @createDate 2025-02-19 10:19:26
*/
public interface DfExcelC98bInkOilConfigService extends IService<DfExcelC98bInkOilConfig> {


    public int importOilConfigFromExcel(MultipartFile file, String batchId);
}
