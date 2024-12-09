package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * cg电阻
 * @TableName df_up_cg_resistance
 */
@TableName(value ="df_up_cg_resistance")
@Data
public class DfUpCgResistance implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测试日期
     */
    @TableField(value = "test_date")
    private String testDate;

    /**
     * 测试时间
     */
    @TableField(value = "test_time")
    private String testTime;

    /**
     * 班次
     */
    @TableField(value = "rank_times")
    private String rankTimes;

    /**
     * 工厂
     */
    @TableField(value = "factory")
    private String factory;

    /**
     * 项目
     */
    @TableField(value = "project")
    private String project;

    /**
     * 阶段
     */
    @TableField(value = "stage")
    private String stage;

    /**
     * 线体
     */
    @TableField(value = "linebody")
    private String linebody;

    /**
     * 250v电阻
     */
    @TableField(value = "resistance1")
    private Double resistance1;

    /**
     * 1000v电阻
     */
    @TableField(value = "resistance2")
    private Double resistance2;

    /**
     * 油墨批次
     */
    @TableField(value = "BMbatch")
    private String bmbatch;

    /**
     * 罐号
     */
    @TableField(value = "can_number")
    private String canNumber;

    /**
     * 数据上传人
     */
    @TableField(value = "upload_name")
    private String uploadName;

    /**
     * 批次号（时间+次数）
     */
    @TableField(value = "batch_id")
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}