package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm2】的数据库操作Service
* @createDate 2025-02-18 11:57:38
*/
public interface DfExcelC98bInkBm2Service extends IService<DfExcelC98bInkBm2> {

    public Map<String, Object> importExcelData(MultipartFile file, String batchId);

}
