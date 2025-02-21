package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bLayer2Program20240624ManualConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_layer2_program_20240624_manual_config(C98B二层程序20240624（手动）相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:37
*/
public interface DfExcelC98bLayer2Program20240624ManualConfigService extends IService<DfExcelC98bLayer2Program20240624ManualConfig> {

    int importConfigFromExcel(MultipartFile file,String batchId);;


}
