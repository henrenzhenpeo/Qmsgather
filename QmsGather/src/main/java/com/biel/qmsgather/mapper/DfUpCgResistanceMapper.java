package com.biel.qmsgather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biel.qmsgather.domain.upweb.DfUpCgResistance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author dafenqi
* @description 针对表【df_up_cg_resistance(cg电阻)】的数据库操作Mapper
* @createDate 2024-11-27 10:05:36
* @Entity com.biel.gateway.domain.upweb.DfUpCgResistance
*/
@Mapper
public interface DfUpCgResistanceMapper extends BaseMapper<DfUpCgResistance> {
    @Select("SELECT batch_id FROM df_up_cg_resistance WHERE DATE(test_date) = CURDATE() ORDER BY batch_id DESC LIMIT 1")
    public String getMaxBatchId();
}




