package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_bm1
 */
@TableName(value ="df_excel_c98b_ink_bm1")
@Data
public class DfExcelC98bInkBm1 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String time;

    /**
     * 
     */
    private BigDecimal j3;

    /**
     * 
     */
    private BigDecimal j8;

    /**
     * 
     */
    private BigDecimal j13;

    /**
     * 
     */
    private BigDecimal j17;

    /**
     * 
     */
    private BigDecimal average;

    /**
     * 
     */
    private String result;

    /**
     * 
     */
    private Integer machineNumber;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String notes;

    private String BatchId;

    private LocalDateTime timeVar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}