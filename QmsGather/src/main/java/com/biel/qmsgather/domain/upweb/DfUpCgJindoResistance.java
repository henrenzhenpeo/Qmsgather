package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * jindo电阻
 * @TableName df_up_cg_jindo_resistance
 */
@TableName(value ="df_up_cg_jindo_resistance")
@Data
public class DfUpCgJindoResistance implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * test_date
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
     * 电阻250v
     */
    @TableField(value = "sdresistance1")
    private String sdresistance1;

    /**
     * 电阻1000v
     */
    @TableField(value = "sdresistance2")
    private String sdresistance2;

    /**
     * fd电阻250v
     */
    @TableField(value = "fdresistance1")
    private String fdresistance1;

    /**
     * fd电阻1000v
     */
    @TableField(value = "fdresistance2")
    private String fdresistance2;

    /**
     * 油墨批次号
     */
    @TableField(value = "BMbatch")
    private String bmbatch;

    /**
     * 罐号
     */
    @TableField(value = "can_number")
    private String canNumber;

    /**
     * 批次号
     */
    @TableField(value = "batch_id")
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 数据上传人
     */
    @TableField(value = "upload_name")
    private String uploadName;

}