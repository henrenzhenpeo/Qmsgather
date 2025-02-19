package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bWireframeInkClimbingHeight;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author 96901
* @description 针对表【df_excel_c98b_wireframe_ink_climbing_height】的数据库操作Service
* @createDate 2025-02-13 10:07:17
*/
public interface DfExcelC98bWireframeInkClimbingHeightService extends IService<DfExcelC98bWireframeInkClimbingHeight> {


    /**
     * 从Excel文件导入数据
     * @param file 上传的Excel文件
     * @return 导入的数据条数
     */
    int importFromExcel(MultipartFile file);

    /**
     * 批量保存数据
     * @param dataList 数据列表
     * @return 保存成功的数据条数
     */
    int batchSave(List<DfExcelC98bWireframeInkClimbingHeight> dataList);


}
