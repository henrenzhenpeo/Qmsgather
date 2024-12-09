package com.biel.qmsgather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biel.qmsgather.domain.upweb.DfUpCgJindoResistance;

/**
* @author dafenqi
* @description 针对表【df_up_cg_jindo_resistance(jindo电阻)】的数据库操作Service
* @createDate 2024-11-27 15:28:00
*/
public interface DfUpCgJindoResistanceService extends IService<DfUpCgJindoResistance> {
    String getMaxBatchId();
}
