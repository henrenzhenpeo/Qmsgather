package com.biel.qmsgather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624ManualConfig;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_20240624_manual_config】的数据库操作Service
* @createDate 2025-02-20 15:34:45
*/
public interface DfExcelC98bWireframe20240624ManualConfigService extends IService<DfExcelC98bWireframe20240624ManualConfig> {
    public int importConfigFromExcel(MultipartFile file,String batchId);

}
