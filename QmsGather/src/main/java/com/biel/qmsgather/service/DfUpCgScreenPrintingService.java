package com.biel.qmsgather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biel.qmsgather.domain.upweb.DfUpCgScreenPrinting;

/**
* @author dafenqi
* @description 针对表【df_up_cg_screen_printing(丝印过程管控)】的数据库操作Service
* @createDate 2024-11-27 10:05:48
*/
public interface DfUpCgScreenPrintingService extends IService<DfUpCgScreenPrinting> {
    String getMaxBatchId();
}
