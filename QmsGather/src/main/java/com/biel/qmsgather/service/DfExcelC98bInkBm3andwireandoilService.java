package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwireandoil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andwireandoil】的数据库操作Service
* @createDate 2025-02-19 10:18:57
*/
public interface DfExcelC98bInkBm3andwireandoilService extends IService<DfExcelC98bInkBm3andwireandoil> {


    public int importExcelData(MultipartFile file);
}
