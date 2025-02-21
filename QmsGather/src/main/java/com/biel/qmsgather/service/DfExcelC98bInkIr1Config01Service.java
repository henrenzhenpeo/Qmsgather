package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkIr1Config01;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir1_config_01】的数据库操作Service
* @createDate 2025-02-21 10:03:17
*/
public interface DfExcelC98bInkIr1Config01Service extends IService<DfExcelC98bInkIr1Config01> {

    public int importConfigFromExcel(MultipartFile file, String batchId);

}
