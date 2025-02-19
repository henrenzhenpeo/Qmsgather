package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkIr1;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_ir1】的数据库操作Service
* @createDate 2025-02-19 10:19:12
*/
public interface DfExcelC98bInkIr1Service extends IService<DfExcelC98bInkIr1> {

    public int importFromExcel(MultipartFile file);

}
