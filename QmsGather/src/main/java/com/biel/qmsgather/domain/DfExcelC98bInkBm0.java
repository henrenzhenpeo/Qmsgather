package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_bm0
 */
@TableName(value ="df_excel_c98b_ink_bm0")
@Data
public class DfExcelC98bInkBm0 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String measurementTime;

    /**
     * 
     */
    private Double sd1;

    /**
     * 
     */
    private Double sd3;

    /**
     * 
     */
    private Double sd5;

    /**
     * 
     */
    private Double sd7;

    /**
     * 
     */
    private Double fd1;

    /**
     * 
     */
    private Double fd3;

    /**
     * 
     */
    @TableField("average")
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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}