package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwireConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andwire_config】的数据库操作Service
* @createDate 2025-02-19 10:18:54
*/
public interface DfExcelC98bInkBm3andwireConfigService extends IService<DfExcelC98bInkBm3andwireConfig> {

    public int importConfigFromExcel(MultipartFile file);






}
