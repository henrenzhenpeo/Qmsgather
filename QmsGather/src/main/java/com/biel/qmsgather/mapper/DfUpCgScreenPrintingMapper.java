package com.biel.qmsgather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biel.qmsgather.domain.upweb.DfUpCgScreenPrinting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author dafenqi
* @description 针对表【df_up_cg_screen_printing(丝印过程管控)】的数据库操作Mapper
* @createDate 2024-11-27 10:05:48
* @Entity com.biel.gateway.domain.upweb.DfUpCgScreenPrinting
*/
@Mapper
public interface DfUpCgScreenPrintingMapper extends BaseMapper<DfUpCgScreenPrinting> {
    @Select("SELECT batch_id FROM df_up_cg_screen_printing WHERE DATE(test_time) = CURDATE() ORDER BY batch_id DESC LIMIT 1")
    public String getMaxBatchId();
}




