package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfYfExcelSandblastingUploadTemplateData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_yf_excel_sandblasting_upload_template_data(水质数据表)】的数据库操作Service
* @createDate 2025-02-25 09:43:47
*/
public interface DfYfExcelSandblastingUploadTemplateDataService extends IService<DfYfExcelSandblastingUploadTemplateData> {



    public int importDataFromExcel(MultipartFile file);

}
