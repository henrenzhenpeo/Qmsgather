package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bWireframeInkClimbingHeightConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_ink_climbing_height_config】的数据库操作Service
* @createDate 2025-02-13 10:07:21
*/
public interface DfExcelC98bWireframeInkClimbingHeightConfigService extends IService<DfExcelC98bWireframeInkClimbingHeightConfig> {


    /**
     * 从Excel导入配置数据（第3-4行）
     * @param file Excel文件
     * @return 成功导入的记录数
     */
    int importConfigFromExcel(MultipartFile file);












}
