package com.biel.qmsgather.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class DfUpCgScreenPrintingValueDto {

    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 工厂
     */
    @TableField(value = "factory")
    private String factory;

    /**
     * 项目
     */
    @TableField(value = "project")
    private String project;

    /**
     * 阶段
     */
    @TableField(value = "stage")
    private String stage;

    /**
     * 过程编号
     */
    @TableField(value = "process")
    private String process;
    /**
     * 日期（yyyy-mm-dd）
     */
    private String startTestDate;
    private String endTestDate;

}
