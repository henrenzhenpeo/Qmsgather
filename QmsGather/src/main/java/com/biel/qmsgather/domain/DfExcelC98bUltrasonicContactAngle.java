package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ultrasonic_contact_angle
 */
@TableName(value ="df_excel_c98b_ultrasonic_contact_angle")
@Data
public class DfExcelC98bUltrasonicContactAngle implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Date measurementTime;

    /**
     * 
     */
    private BigDecimal grooveEdge;

    /**
     * 
     */
    private BigDecimal grooveRightAngle;

    /**
     * 
     */
    private BigDecimal upperLength;

    /**
     * 
     */
    private BigDecimal nonGrooveRightAngle;

    /**
     * 
     */
    private BigDecimal nonGrooveEdge;

    /**
     * 
     */
    private BigDecimal nonGrooveLeftAngle;

    /**
     * 
     */
    private BigDecimal lowerLength;

    /**
     * 
     */
    private BigDecimal grooveLeftAngle;

    /**
     * 
     */
    private Integer machineNumber;

    /**
     * 
     */
    private String status;


    private String noted;

    private String BatchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}