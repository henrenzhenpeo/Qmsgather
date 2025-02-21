package com.biel.qmsgather.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * C98B二维码位置度（孔向左）8倍Z最新不动相关数据
 * @TableName df_excel_c98b_qr_code_position_hole_left_8x_z_newest_fixed
 */
@TableName(value ="df_excel_c98b_qr_code_position_hole_left_8x_z_newest_fixed")
@Data
public class DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed implements Serializable {




    @TableId(type = IdType.AUTO)
    private Integer id;



    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 2D码高
     */
    private BigDecimal twoDCodeHeight;

    /**
     * 2D码宽
     */
    private BigDecimal twoDCodeWidth;

    /**
     * 2D码中心到基准C
     */
    private BigDecimal twoDCodeCenterToC;

    /**
     * 2D镭码中心到玻璃Y的距离
     */
    private BigDecimal twoDCodeCenterToGlassY;

    /**
     * 2D镭码中心到BM1
     */
    private BigDecimal twoDCodeCenterToBm1;

    /**
     * 苹果logo高度
     */
    private BigDecimal appleLogoHeight;

    /**
     * 苹果宽
     */
    private BigDecimal appleLogoWidth;

    /**
     * 苹果中心到玻璃Y
     */

    private BigDecimal appleCenterToGlassX;

    /**
     * 苹果中心到玻璃Y
     */
    private BigDecimal appleCenterToGlassY;

    /**
     * 叶子宽
     */
    private BigDecimal leafWidth;

    /**
     * 叶子底部到苹果logo底部
     */
    private BigDecimal leafBottomToAppleLogoBottom;

    /**
     * 叶子顶部到苹果logo底部
     */
    private BigDecimal leafTopToAppleLogoBottom;

    /**
     * 2D镭码中心到光油
     */
    private BigDecimal twoDCodeCenterToOil;

    /**
     * 叶子顶部到基准C
     */
    private BigDecimal leafTopToC;

    /**
     * 机台号
     */
    private String machineNumber;

    /**
     * 状态
     */
    private String status;


    private String BatchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}