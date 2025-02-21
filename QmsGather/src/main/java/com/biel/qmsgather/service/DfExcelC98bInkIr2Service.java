package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkIr2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir2】的数据库操作Service
* @createDate 2025-02-19 10:19:18
*/
public interface DfExcelC98bInkIr2Service extends IService<DfExcelC98bInkIr2> {

    public Map<String, Object> importExcelData(MultipartFile file, String batchId);

}
