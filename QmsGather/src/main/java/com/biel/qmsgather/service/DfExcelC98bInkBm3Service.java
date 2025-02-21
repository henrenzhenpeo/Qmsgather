package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm3;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3】的数据库操作Service
* @createDate 2025-02-18 11:57:44
*/
public interface DfExcelC98bInkBm3Service extends IService<DfExcelC98bInkBm3> {
    public int importExcelData(MultipartFile file, String batchId);
}
