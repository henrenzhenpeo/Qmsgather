package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bUltrasonicContactAngle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biel.qmsgather.util.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
* @author 96901
* @description 针对表【df_excel_c98b_ultrasonic_contact_angle】的数据库操作Service
* @createDate 2025-02-12 10:23:26
*/
public interface DfExcelC98bUltrasonicContactAngleService extends IService<DfExcelC98bUltrasonicContactAngle> {


    public Map<String, Object> importExcelFile(MultipartFile file,String batchId)throws IOException;



}
