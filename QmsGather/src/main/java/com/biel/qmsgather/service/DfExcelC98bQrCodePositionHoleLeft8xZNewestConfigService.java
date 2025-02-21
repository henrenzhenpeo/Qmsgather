package com.biel.qmsgather.service;

import com.biel.qmsgather.domain.DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 96901
* @description 针对表【df_excel_c98b_qr_code_position_hole_left_8x_z_newest_config(C98B二维码位置度（孔向左）8倍Z最新不动相关数据)】的数据库操作Service
* @createDate 2025-02-13 11:12:44
*/
public interface DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigService extends IService<DfExcelC98bQrCodePositionHoleLeft8xZNewestConfig> {



    public int importConfigFromExcel(MultipartFile file,String batchId);
}
