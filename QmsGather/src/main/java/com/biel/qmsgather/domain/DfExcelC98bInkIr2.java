package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_ir2
 */
@TableName(value ="df_excel_c98b_ink_ir2")
@Data
public class DfExcelC98bInkIr2 implements Serializable {
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
    private String rx;

    /**
     * 
     */
    private String tx;

    /**
     * 
     */
    private String average;

    /**
     * 
     */
    private String testResult;

    /**
     * 
     */
    private String machineNumber;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String noted;


    private String BatchId;

    private LocalDateTime timeVar;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}