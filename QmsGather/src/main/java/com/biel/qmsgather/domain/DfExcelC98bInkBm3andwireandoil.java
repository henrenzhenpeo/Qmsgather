package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_bm3andwireandoil
 */
@TableName(value ="df_excel_c98b_ink_bm3andwireandoil")
@Data
public class DfExcelC98bInkBm3andwireandoil implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String recordTime;

    /**
     * 
     */
    private String j2p;

    /**
     * 
     */
    private String j5p;

    /**
     * 
     */
    private String j8p;

    /**
     * 
     */
    private String j19p;

    /**
     * 
     */
    private String j21p;

    /**
     * 
     */
    private String j17p;

    /**
     * 
     */
    private String j3;

    /**
     * 
     */
    private String j8;

    /**
     * 
     */
    private String j13;

    /**
     * 
     */
    private String j17;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}