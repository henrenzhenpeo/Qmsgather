package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * C98B线框20240624（手动）相关数据
 * @TableName df_excel_c98b_wireframe_20240624_manual
 */
@TableName(value ="df_excel_c98b_wireframe_20240624_manual")
@Data
public class DfExcelC98bWireframe20240624Manual implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 
     */
    private String y1;

    /**
     * 
     */
    private String y1XF;

    /**
     * 
     */
    private String y2;

    /**
     * 
     */
    private String xdz3;

    /**
     * 
     */
    private String xdz4;

    /**
     * 
     */
    private String y25y2;

    /**
     * 
     */
    private String y284;

    /**
     * 
     */
    private String y26y1;

    /**
     * 
     */
    private String x7;

    /**
     * 
     */
    private String x8;

    /**
     * 
     */
    private String r1;

    /**
     * 
     */
    private String r2;

    /**
     * 
     */
    private String r3;

    /**
     * 
     */
    private String r4;

    /**
     * 
     */
    private String shapeLength1;

    /**
     * 
     */
    private String shapeLength2;

    /**
     * 
     */
    private String outlineWidth94;

    /**
     * 外形宽 - 95
     */
    private String outlineWidth95;

    /**
     * 外形宽 - 96
     */
    private String outlineWidth96;

    /**
     * 凹槽宽 - 97
     */
    private String grooveWidth97;

    /**
     * 凹槽宽 - 98
     */
    private String grooveWidth98;

    /**
     * 凹槽宽 - 99
     */
    private String grooveWidth99;

    /**
     * 凹槽宽 - 100
     */
    private String grooveWidth100;

    /**
     * 凹槽宽 - 101
     */
    private String grooveWidth101;

    /**
     * 凹槽长 - 102
     */
    private String grooveLength102;

    /**
     * 全身油墨 - 103
     */
    private String allBodyInk103;

    /**
     * 长差值 
     */
    private String lengthDifference;

    /**
     * 宽差值
     */
    private String widthDifference;

    /**
     * 凹槽宽差
     */
    private String grooveWidthDifference;

    /**
     * 机台号
     */
    private String machineNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 
     */
    private String noted;

    /**
     * 
     */
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}