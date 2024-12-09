package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 丝印过程管控
 * @TableName df_up_cg_screen_printing
 */
@TableName(value ="df_up_cg_screen_printing")
@Data
public class DfUpCgScreenPrinting implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 仪器编号
     */
    @TableField(value = "instrument_id")
    private Integer instrumentId;

    /**
     * 检测项目
     */
    @TableField(value = "test_items")
    private String testItems;

    /**
     * 判定 ok or ng
     */
    @TableField(value = "judge")
    private String judge;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 过程编号（BM，BMO，IR）
     */
    @TableField(value = "process")
    private String process;

    /**
     * 测试时间
     */
    @TableField(value = "test_time")
    private String testTime;

    /**
     * 数据上传人
     */
    @TableField(value = "upload_name")
    private String uploadName;

    /**
     * 批次号（时间+次数）
     */
    @TableField(value = "batch_id")
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}