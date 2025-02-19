package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bRCornerGroove;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_r_corner_groove(C98B线框R角与凹槽相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:53
*/
public interface DfExcelC98bRCornerGrooveService extends IService<DfExcelC98bRCornerGroove> {


    int importExcelData(MultipartFile file);


}
