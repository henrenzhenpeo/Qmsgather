package com.biel.qmsgather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biel.qmsgather.domain.upweb.DfUpCgResistance;

/**
* @author dafenqi
* @description 针对表【df_up_cg_resistance(cg电阻)】的数据库操作Service
* @createDate 2024-11-27 10:05:36
*/
public interface DfUpCgResistanceService extends IService<DfUpCgResistance> {
    String getMaxBatchId();
}
