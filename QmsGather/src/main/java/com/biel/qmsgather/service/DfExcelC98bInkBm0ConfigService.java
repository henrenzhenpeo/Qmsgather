package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm0Config;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm0_config】的数据库操作Service
* @createDate 2025-02-18 11:57:27
*/
public interface DfExcelC98bInkBm0ConfigService extends IService<DfExcelC98bInkBm0Config> {


    public int importConfigFromExcel(MultipartFile file, String batchId);
}
