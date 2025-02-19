package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_wireframe_20240624_manual_config
 */
@TableName(value ="df_excel_c98b_wireframe_20240624_manual_config")
@Data
public class DfExcelC98bWireframe20240624ManualConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    private String standardType;

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
    private String allBodyInk103;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}