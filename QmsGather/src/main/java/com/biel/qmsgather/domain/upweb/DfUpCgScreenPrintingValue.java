package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 油墨密度参数值
 * @TableName df_up_cg_screen_printing_value
 */
@TableName(value ="df_up_cg_screen_printing_value")
@Data
public class DfUpCgScreenPrintingValue implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 批次号
     */
    @TableField(value = "batch_id")
    private String batchId;

    /**
     * 最大值
     */
    @TableField(value = "max_value")
    private Double maxValue;

    /**
     * 最小值
     */
    @TableField(value = "min_value")
    private Double minValue;

    /**
     * 平均值
     */
    @TableField(value = "ave_value")
    private Double aveValue;

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
     * 过程编号
     */
    @TableField(value = "process")
    private String process;

    /**
     * 仪器编号
     */
    @TableField(value = "instrument_id")
    private String instrumentId;

    /**
     * 检测项目
     */
    @TableField(value = "test_items")
    private String testItems;

    /**
     * 测试时间
     */
    @TableField(value = "test_time")
    private String testTime;

    /**
     * 数据上传人
     */
    @TableField(value = "upload_name")
    private String uploadName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<DfUpCgScreenPrinting> dfUpCgScreenPrintingList;
}