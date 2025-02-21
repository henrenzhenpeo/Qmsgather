package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName df_excel_c98b_wireframe_ink_climbing_height_config
 */
@TableName(value ="df_excel_c98b_wireframe_ink_climbing_height_config")
@Data
public class DfExcelC98bWireframeInkClimbingHeightConfig implements Serializable {



    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private String standardType;

    /**
     * 上长边企身油墨到玻璃正面高度
     */
    private String upperLongSideInkToGlassFrontHeight;

    /**
     * 下长边企身油墨到玻璃正面高度
     */
    private String lowerLongSideInkToGlassFrontHeight;

    /**
     * 非凹槽短边企身油墨到玻璃正面高度
     */
    private String nonGrooveShortSideInkToGlassFrontHeight;

    /**
     * 凹槽短边企身油墨到玻璃正面高度
     */
    private String grooveShortSideInkToGlassFrontHeight;

    /**
     * 凹槽企身油墨到玻璃背面高度
     */
    private String grooveInkToGlassBackHeight;

    private String BatchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}