package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bLayer2Program20240624Manual;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_layer2_program_20240624_manual(C98B二层程序20240624（手动）相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:33
*/
public interface DfExcelC98bLayer2Program20240624ManualService extends IService<DfExcelC98bLayer2Program20240624Manual> {



    void importExcelData(MultipartFile file, String batchId) throws Exception;



}
