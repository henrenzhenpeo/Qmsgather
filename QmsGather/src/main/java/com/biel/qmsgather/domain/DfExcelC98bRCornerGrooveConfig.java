package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * C98B线框R角与凹槽相关数据
 * @TableName df_excel_c98b_r_corner_groove_config
 */
@TableName(value ="df_excel_c98b_r_corner_groove_config")
@Data
public class DfExcelC98bRCornerGrooveConfig implements Serializable {



    @TableId(type = IdType.AUTO)
    private Integer id;



    /**
     * 记录时间
     */
    private String standardType;

    /**
     * R角 - 8
     */
    private BigDecimal r8;

    /**
     * R角 - R1
     */
    private BigDecimal r1;

    /**
     * R角 - R2
     */
    private BigDecimal r2;

    /**
     * R角 - R3
     */
    private BigDecimal r3;

    /**
     * R角 - R4
     */
    private BigDecimal r4;

    /**
     * 凹槽R角 - 右外
     */
    private BigDecimal rightOutside;

    /**
     * 凹槽R角 - 右内
     */
    private BigDecimal rightInside;

    /**
     * 凹槽R角 - 左内
     */
    private BigDecimal leftInside;

    /**
     * 凹槽R角 - 左外
     */
    private BigDecimal leftOutside;

    /**
     * 凹槽1
     */
    private BigDecimal groove1;

    /**
     * 凹槽2
     */
    private BigDecimal groove2;

    /**
     * 凹槽3
     */
    private BigDecimal groove3;

    /**
     * 凹槽4
     */
    private BigDecimal groove4;

    /**
     * 凹槽5
     */
    private BigDecimal groove5;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}