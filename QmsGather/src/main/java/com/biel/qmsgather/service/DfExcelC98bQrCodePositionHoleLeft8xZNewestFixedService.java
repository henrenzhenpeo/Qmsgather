package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_qr_code_position_hole_left_8x_z_newest_fixed(C98B二维码位置度（孔向左）8倍Z最新不动相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:46
*/
public interface DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedService extends IService<DfExcelC98bQrCodePositionHoleLeft8xZNewestFixed> {


    public int importExcelData(MultipartFile file);


}
