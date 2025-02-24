package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * C98B二层程序20240624（手动）相关数据
 * @TableName df_excel_c98b_layer2_program_20240624_manual
 */
@TableName(value ="df_excel_c98b_layer2_program_20240624_manual")
@Data
public class DfExcelC98bLayer2Program20240624Manual implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 位置 - 1Y2大F
     */
    private BigDecimal position28;

    /**
     * 位置 - 中
     */
    private BigDecimal position29;

    /**
     * 位置 - 2Y1
     */
    private BigDecimal position30;

    /**
     * 位置 - 3X大Z
     */
    private BigDecimal position31;

    /**
     * 位置 - 4X
     */
    private BigDecimal position32;

    /**
     * 位置 - 5Y大Z
     */
    private BigDecimal position33;

    /**
     * 位置 - 中（再次出现）
     */
    private BigDecimal position34;

    /**
     * 位置 - 6Y
     */
    private BigDecimal position35;

    /**
     * 位置 - 7X大F
     */
    private BigDecimal position36;

    /**
     * 位置 - 8X
     */
    private BigDecimal position37;

    /**
     * 机台号
     */
    private String machineNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 
     */
    private String noted;

    /**
     * 批次号
     */
    private String batchId;


    private LocalDateTime timeVar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}