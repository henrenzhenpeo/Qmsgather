package com.biel.qmsgather.domain.dto;

import lombok.Data;

@Data
public class DfUpCgJindoResistanceDto {

    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 项目
     */
    private String project;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 阶段
     */
    private String stage;

    /**
     * 日期（yyyy-mm-dd）
     */
    private String startTestDate;
    private String endTestDate;

}
