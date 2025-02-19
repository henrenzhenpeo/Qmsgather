package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bWireframe20240624Manual;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_20240624_manual(C98B线框20240624（手动）相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:13:11
*/
public interface DfExcelC98bWireframe20240624ManualService extends IService<DfExcelC98bWireframe20240624Manual> {


    int importExcelData(MultipartFile file);


}
