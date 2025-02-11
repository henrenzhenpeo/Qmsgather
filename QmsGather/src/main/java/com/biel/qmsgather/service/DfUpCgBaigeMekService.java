package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dafenqi
* @description 针对表【df_up_cg_baige_mek(cg 百格测试mek)】的数据库操作Service
* @createDate 2024-12-19 09:10:44
*/
public interface DfUpCgBaigeMekService extends IService<DfUpCgBaigeMek> {

    String getMaxBatchId();
}
