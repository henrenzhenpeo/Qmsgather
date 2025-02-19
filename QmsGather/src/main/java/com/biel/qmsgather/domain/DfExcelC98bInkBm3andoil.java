package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_bm3andoil
 */
@TableName(value ="df_excel_c98b_ink_bm3andoil")
@Data
public class DfExcelC98bInkBm3andoil implements Serializable {
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
    private String average;

    /**
     * 
     */
    private String result;

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