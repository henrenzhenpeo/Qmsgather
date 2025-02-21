package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelProcesscontrolconfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_processcontrolconfig】的数据库操作Service
* @createDate 2025-02-12 09:43:57
*/
public interface DfExcelProcesscontrolconfigService extends IService<DfExcelProcesscontrolconfig> {

    public Map<String, Object> importExcelData(MultipartFile file, String batchId);

}
