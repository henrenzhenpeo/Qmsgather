package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bInkBm3andwire;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ink_bm3andwire】的数据库操作Service
* @createDate 2025-02-19 10:18:51
*/
public interface DfExcelC98bInkBm3andwireService extends IService<DfExcelC98bInkBm3andwire> {

    public Map<String, Object> importExcelData(MultipartFile file);

}
