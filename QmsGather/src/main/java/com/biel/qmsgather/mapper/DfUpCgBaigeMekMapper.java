package com.biel.qmsgather.mapper;

import com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author dafenqi
* @description 针对表【df_up_cg_baige_mek(cg 百格测试mek)】的数据库操作Mapper
* @createDate 2024-12-19 09:10:44
* @Entity com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek
*/
@Mapper
public interface DfUpCgBaigeMekMapper extends BaseMapper<DfUpCgBaigeMek> {

    @Select("SELECT batch_id FROM df_up_cg_baige_mek WHERE DATE(test_date) = CURDATE() ORDER BY batch_id DESC LIMIT 1")
    public String getMaxBatchId();
}




