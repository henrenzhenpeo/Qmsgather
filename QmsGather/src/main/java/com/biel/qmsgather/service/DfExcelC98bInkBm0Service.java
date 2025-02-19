package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm0;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm0】的数据库操作Service
* @createDate 2025-02-18 11:57:22
*/
public interface DfExcelC98bInkBm0Service extends IService<DfExcelC98bInkBm0> {

    public Map<String, Object> importExcelData(MultipartFile file);

}
