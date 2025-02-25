package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 水质数据表
 * @TableName df_yf_excel_sandblasting_upload_template_data
 */
@TableName(value ="df_yf_excel_sandblasting_upload_template_data")
@Data
public class DfYfExcelSandblastingUploadTemplateData implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 测试时间
     */
    private String testTime;

    /**
     * 清晰度
     */
    private String clarity;

    /**
     * 颗粒度
     */
    private String granularity;

    /**
     * 光泽度
     */
    private String luminosity;

    /**
     * 机台号
     */
    private String machineNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 测量员
     */
    private String tester;

    /**
     * 
     */
    private String redundant1;

    /**
     * 
     */
    private String redundant2;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}