package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_bm2_config_01
 */
@TableName(value ="df_excel_c98b_ink_bm2_config_01")
@Data
public class DfExcelC98bInkBm2Config01 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    private String modelCode;

    /**
     * 测量仪器
     */
    private String measuringInstrument;

    /**
     * 标称值
     */
    private String nominalValue;

    /**
     * 最大公差
     */
    private String toleranceMax;

    /**
     * 最小公差
     */
    private String toleranceMin;

    /**
     * USL上限
     */
    private String upperLimit;

    /**
     * LSL下限
     */
    private String lowerLimit;

    /**
     * 
     */
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}