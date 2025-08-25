package com.biel.qmsgather.domain.dto;

import lombok.Data;

@Data
public class DfUpCgBaigeMekDto {


    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 工序
     */
    private String process;

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
