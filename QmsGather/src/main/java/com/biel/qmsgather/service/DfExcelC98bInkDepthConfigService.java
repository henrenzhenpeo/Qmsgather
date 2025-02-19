package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkDepthConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_depth_config】的数据库操作Service
* @createDate 2025-02-19 10:19:09
*/
public interface DfExcelC98bInkDepthConfigService extends IService<DfExcelC98bInkDepthConfig> {

    public int importConfigFromExcel(MultipartFile file);

}
