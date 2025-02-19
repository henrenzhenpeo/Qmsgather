package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bRCornerGrooveConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_r_corner_groove_config(C98B线框R角与凹槽相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:55
*/
public interface DfExcelC98bRCornerGrooveConfigService extends IService<DfExcelC98bRCornerGrooveConfig> {

    public int importExcelData(MultipartFile file);


}
