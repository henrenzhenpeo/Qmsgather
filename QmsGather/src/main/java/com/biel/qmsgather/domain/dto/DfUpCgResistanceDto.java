package com.biel.qmsgather.domain.dto;

import lombok.Data;

@Data
public class DfUpCgResistanceDto {

    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

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
