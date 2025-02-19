package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_oil
 */
@TableName(value ="df_excel_c98b_ink_oil")
@Data
public class DfExcelC98bInkOil implements Serializable {
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