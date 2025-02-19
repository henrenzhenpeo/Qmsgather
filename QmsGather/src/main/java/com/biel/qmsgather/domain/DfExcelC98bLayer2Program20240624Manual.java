package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * C98B二层程序20240624（手动）相关数据
 * @TableName df_excel_c98b_layer2_program_20240624_manual
 */
@TableName(value ="df_excel_c98b_layer2_program_20240624_manual")
@Data
public class DfExcelC98bLayer2Program20240624Manual implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 记录时间
     */
    @TableField("record_time")
    private String recordTime;

    /**
     * 位置 - 1Y2大F
     */
    @TableField("position_28")
    private BigDecimal position28;

    /**
     * 位置 - 中
     */
    @TableField("position_29")
    private BigDecimal position29;

    /**
     * 位置 - 2Y1
     */
    @TableField("position_30")
    private BigDecimal position30;

    /**
     * 位置 - 3X大Z
     */
    @TableField("position_31")
    private BigDecimal position31;

    /**
     * 位置 - 4X
     */
    @TableField("position_32")
    private BigDecimal position32;

    /**
     * 位置 - 5Y大Z
     */
    @TableField("position_33")
    private BigDecimal position33;

    /**
     * 位置 - 中（再次出现）
     */
    @TableField("position_34")
    private BigDecimal position34;

    /**
     * 位置 - 6Y
     */
    @TableField("position_35")
    private BigDecimal position35;

    /**
     * 位置 - 7X大F
     */
    @TableField("position_36")
    private BigDecimal position36;

    /**
     * 位置 - 8X
     */
    @TableField("position_37")
    private BigDecimal position37;

    /**
     * 机台号
     */
    @TableField("machine_number")
    private String machineNumber;

    /**
     * 状态
     */
    private String status;

    /**
     *
     */
    private String noted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}