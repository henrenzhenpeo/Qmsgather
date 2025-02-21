package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm3andoil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andoil】的数据库操作Service
* @createDate 2025-02-19 10:18:41
*/
public interface DfExcelC98bInkBm3andoilService extends IService<DfExcelC98bInkBm3andoil> {

    public Map<String, Object> importExcelData(MultipartFile file, String batchId);

}
