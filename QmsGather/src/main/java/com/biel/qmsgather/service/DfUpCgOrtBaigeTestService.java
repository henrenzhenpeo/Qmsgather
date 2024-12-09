package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.upweb.DfUpCgOrtBaigeTest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dafenqi
* @description 针对表【df_up_cg_ort_baige_test(千百格测试)】的数据库操作Service
* @createDate 2024-12-09 15:59:43
*/
public interface DfUpCgOrtBaigeTestService extends IService<DfUpCgOrtBaigeTest> {
    String getMaxBatchId();
}
