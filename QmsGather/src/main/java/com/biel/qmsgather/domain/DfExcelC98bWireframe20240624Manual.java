package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * C98B线框20240624（手动）相关数据
 * @TableName df_excel_c98b_wireframe_20240624_manual
 */
@TableName(value ="df_excel_c98b_wireframe_20240624_manual")
@Data
public class DfExcelC98bWireframe20240624Manual implements Serializable {


    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 外形宽 - 95
     */
    private BigDecimal outlineWidth95;

    /**
     * 外形宽 - 96
     */
    private BigDecimal outlineWidth96;

    /**
     * 凹槽宽 - 97
     */
    private BigDecimal grooveWidth97;

    /**
     * 凹槽宽 - 98
     */
    private BigDecimal grooveWidth98;

    /**
     * 凹槽宽 - 99
     */
    private BigDecimal grooveWidth99;

    /**
     * 凹槽宽 - 100
     */
    private BigDecimal grooveWidth100;

    /**
     * 凹槽宽 - 101
     */
    private BigDecimal grooveWidth101;

    /**
     * 凹槽长 - 102
     */
    private BigDecimal grooveLength102;

    /**
     * 全身油墨 - 103
     */
    private BigDecimal allBodyInk103;

    /**
     * 长差值 
     */
    private BigDecimal lengthDifference;

    /**
     * 宽差值
     */
    private BigDecimal widthDifference;

    /**
     * 凹槽宽差
     */
    private BigDecimal grooveWidthDifference;

    /**
     * 机台号
     */
    private Integer machineNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 
     */
    private String noted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}