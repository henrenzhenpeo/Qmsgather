package com.biel.qmsgather.mapper;

import com.biel.qmsgather.domain.upweb.DfUpCgOrtBaigeTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author dafenqi
* @description 针对表【df_up_cg_ort_baige_test(千百格测试)】的数据库操作Mapper
* @createDate 2024-12-09 15:59:43
* @Entity com.biel.qmsgather.domain.upweb.DfUpCgOrtBaigeTest
*/
@Mapper
public interface DfUpCgOrtBaigeTestMapper extends BaseMapper<DfUpCgOrtBaigeTest> {
    @Select("SELECT batch_id FROM df_up_cg_ort_baige_test WHERE DATE(measure_date) = CURDATE() ORDER BY batch_id DESC LIMIT 1")
    public String getMaxBatchId();
}




