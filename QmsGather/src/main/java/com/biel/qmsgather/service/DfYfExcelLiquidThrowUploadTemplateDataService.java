package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfYfExcelLiquidThrowUploadTemplateData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_yf_excel_liquid_throw_upload_template_data】的数据库操作Service
* @createDate 2025-02-25 09:43:42
*/
public interface DfYfExcelLiquidThrowUploadTemplateDataService extends IService<DfYfExcelLiquidThrowUploadTemplateData> {

    public int importDataFromExcel(MultipartFile file);

}
