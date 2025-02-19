package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_ink_depth_config
 */
@TableName(value ="df_excel_c98b_ink_depth_config")
@Data
public class DfExcelC98bInkDepthConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String col1;

    /**
     * 
     */
    private String col2;

    /**
     * 
     */
    private String col3;

    /**
     * 
     */
    private String col4;

    /**
     * 
     */
    private String col5;

    /**
     * 
     */
    private String col6;

    /**
     * 
     */
    private String col7;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}