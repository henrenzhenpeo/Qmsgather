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
 * @TableName df_excel_processcontrolconfig
 */
@TableName(value ="df_excel_processcontrolconfig")
@Data
public class DfExcelProcesscontrolconfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String punctuationsize;

    /**
     * 
     */
    private BigDecimal outerlengthupper;

    /**
     * 
     */
    private BigDecimal outerlengthlower;

    /**
     * 
     */
    private BigDecimal outerwidthupper;

    /**
     * 
     */
    private BigDecimal outerwidthlower;

    /**
     * 
     */
    private BigDecimal inlayheight;

    /**
     * 
     */
    private BigDecimal inlaywidth;

    /**
     * 
     */
    private BigDecimal inlaybottomtoglassbottom;

    /**
     * 
     */
    private BigDecimal inlaytoglasscenter;

    /**
     * 
     */
    private BigDecimal inlaytoptoglasstop;

    /**
     * 
     */
    private BigDecimal inlaybottomtoglassbottomdistance;

    /**
     * 
     */
    private String machinenumber;

    /**
     * 
     */
    private String notes;

    /**
     * 
     */
    private String workstation;

    /**
     * 
     */
    private BigDecimal lasercodebottomtoglassbottomMax;

    /**
     * 
     */
    private BigDecimal lasercodebottomtoglassbottomMin;

    /**
     * 
     */
    private BigDecimal lasercodebottomtoglassbottomAvg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}