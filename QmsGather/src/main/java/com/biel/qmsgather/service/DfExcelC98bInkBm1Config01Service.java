package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm1Config01;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm1_config_01】的数据库操作Service
* @createDate 2025-02-21 10:02:51
*/
public interface DfExcelC98bInkBm1Config01Service extends IService<DfExcelC98bInkBm1Config01> {


    public int importConfigFromExcel(MultipartFile file, String batchId);

}
