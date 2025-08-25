package com.biel.qmsgather.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class DfUpCgScreenPrintingDto {

    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 过程编号（BM，BMO，IR）
     */
    private String process;

    /**
     * 日期（yyyy-mm-dd）
     */
    private String startTestDate;
    private String endTestDate;

}
