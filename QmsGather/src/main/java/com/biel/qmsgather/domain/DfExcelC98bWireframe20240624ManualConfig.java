package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName df_excel_c98b_wireframe_20240624_manual_config
 */
@TableName(value ="df_excel_c98b_wireframe_20240624_manual_config")
@Data
public class DfExcelC98bWireframe20240624ManualConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    private String col1;

    /**
     * 外形宽 - 95
     */
    private String col2;

    /**
     * 外形宽 - 96
     */
    private String col3;

    /**
     * 凹槽宽 - 97
     */
    private String col4;

    /**
     * 凹槽宽 - 98
     */
    private String col5;

    /**
     * 凹槽宽 - 99
     */
    private String col6;

    /**
     * 凹槽宽 - 100
     */
    private String col7;

    /**
     * 凹槽宽 - 101
     */
    private String col8;

    /**
     * 凹槽长 - 102
     */
    private String col9;

    /**
     * 全身油墨 - 103
     */
    private String col10;

    /**
     * 长差值 
     */
    private String col11;

    /**
     * 宽差值
     */
    private String col12;

    /**
     * 凹槽宽差
     */
    private String col13;

    /**
     * 
     */
    private String col14;

    /**
     * 
     */
    private String col15;

    /**
     * 
     */
    private String col16;

    /**
     * 
     */
    private String col17;

    /**
     * 
     */
    private String col18;

    /**
     * 
     */
    private String col19;

    /**
     * 
     */
    private String col20;

    /**
     * 
     */
    private String col21;

    /**
     * 
     */
    private String col22;

    /**
     * 
     */
    private String col23;

    /**
     * 
     */
    private String col24;

    /**
     * 
     */
    private String col25;

    /**
     * 
     */
    private String col26;

    /**
     * 
     */
    private String col27;

    /**
     * 
     */
    private String col28;

    /**
     * 
     */
    private String col29;

    /**
     * 
     */
    private String col30;


    private String col31;



    private String col32;


    private String col33;

    private String col34;

    /**
     * 
     */
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}