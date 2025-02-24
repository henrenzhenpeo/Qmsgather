package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_bevel_chamfer_metrics_data
 */
@TableName(value ="df_excel_c98b_bevel_chamfer_metrics_data")
@Data
public class DfExcelC98bBevelChamferMetricsData implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;





    /**
     * 
     */
    private String timepoint;

    /**
     * 
     */
    private BigDecimal leftLongEdgeBevel;

    /**
     * 
     */
    private BigDecimal leftLongEdgeBevel1;

    /**
     * 
     */
    private BigDecimal lowerShortEdgeBevel;

    /**
     * 
     */
    private BigDecimal lowerShortEdgeBevel1;

    /**
     * 
     */
    private BigDecimal rightLongEdgeBevel;

    /**
     * 
     */
    private BigDecimal rightLongEdgeBevel1;

    /**
     * 
     */
    private BigDecimal upperShortEdgeBevel;

    /**
     * 
     */
    private BigDecimal upperShortEdgeBevel1;

    /**
     * 
     */
    private BigDecimal grooveFaceChamfer;

    /**
     * 
     */
    private BigDecimal longEdgeBaseChamfer;

    /**
     * 
     */
    private BigDecimal shortEdgeBaseChamfer;

    /**
     * 
     */
    private BigDecimal grooveBaseChamfer;

    /**
     * 
     */
    private String opportunityBow;

    /**
     * 
     */
    private String periodMachine;

    /**
     * 
     */
    private BigDecimal totalValue;

    /**
     * 
     */
    private BigDecimal bevelEdge;

    /**
     * 
     */
    private BigDecimal chamfer;

    /**
     * 
     */
    private Integer count;


    private String BatchId;






    private LocalDateTime timeVar;








    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}