package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 千百格测试
 * @TableName df_up_cg_ort_baige_test
 */
@TableName(value ="df_up_cg_ort_baige_test")
@Data
public class DfUpCgOrtBaigeTest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 出炉时间
     */
    private String stoveTime;

    /**
     * 烤炉型号（格式：塔式炉/右）
     */
    private String stoveType;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 测试时间
     */
    private String testTime;

    /**
     * 测试完成时间/取出时间
     */
    private String testTimeEnd;

    /**
     * 测试数量/浸泡数量
     */
    private String testNumber;

    /**
     * sample1
     */
    private String sample1;

    /**
     * sample2
     */
    private String sample2;

    /**
     * 测试结果
     */
    private String testResult;

    /**
     * 数据上传人
     */
    private String uploadName;

    /**
     * 批次
     */
    private String batchId;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 阶段
     */
    private String stage;

    /**
     * 测量时间
     */
    private String measureDate;

    /**
     * 工序(mek，千百格，湿百格)
     */
    private String process;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}