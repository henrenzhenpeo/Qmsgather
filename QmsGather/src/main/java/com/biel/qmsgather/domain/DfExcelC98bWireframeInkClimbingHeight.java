package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.sun.org.apache.xpath.internal.objects.XString;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_wireframe_ink_climbing_height
 */
@TableName(value ="df_excel_c98b_wireframe_ink_climbing_height")
@Data
public class DfExcelC98bWireframeInkClimbingHeight implements Serializable {



    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 上长边企身油墨到玻璃正面高度
     */
    private BigDecimal upperLongSideInkToGlassFrontHeight;

    /**
     * 下长边企身油墨到玻璃正面高度
     */
    private BigDecimal lowerLongSideInkToGlassFrontHeight;

    /**
     * 非凹槽短边企身油墨到玻璃正面高度
     */
    private BigDecimal nonGrooveShortSideInkToGlassFrontHeight;

    /**
     * 凹槽短边企身油墨到玻璃正面高度
     */
    private BigDecimal grooveShortSideInkToGlassFrontHeight;

    /**
     * 凹槽企身油墨到玻璃背面高度
     */
    private BigDecimal grooveInkToGlassBackHeight;

    /**
     * 机台号
     */
    private Integer machineNumber;

    /**
     * 状态
     */
    private String status;

    private String BatchId;

    private LocalDateTime timeVar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}