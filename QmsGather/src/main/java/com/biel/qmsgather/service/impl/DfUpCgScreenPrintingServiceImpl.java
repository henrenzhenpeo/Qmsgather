package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.upweb.DfUpCgScreenPrinting;
import com.biel.qmsgather.mapper.DfUpCgScreenPrintingMapper;
import com.biel.qmsgather.service.DfUpCgScreenPrintingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author dafenqi
* @description 针对表【df_up_cg_screen_printing(丝印过程管控)】的数据库操作Service实现
* @createDate 2024-11-27 10:05:48
*/
@Service
@Transactional
public class DfUpCgScreenPrintingServiceImpl extends ServiceImpl<DfUpCgScreenPrintingMapper, DfUpCgScreenPrinting>
    implements DfUpCgScreenPrintingService {


    @Resource
    private DfUpCgScreenPrintingMapper dfUpCgScreenPrintingMapper;

    private String generateBatchId(String maxBatchId) {
        // 获取当天日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        // 如果没有批次号，直接从 1 开始
        int newCount = 1;
        if (maxBatchId != null && maxBatchId.contains("+")) {
            String[] parts = maxBatchId.split("\\+");
            newCount = Integer.parseInt(parts[1]) + 1;
        }

        return today + "+" + newCount;
    }


    @Override
    public String getMaxBatchId() {
        String maxBatchId = dfUpCgScreenPrintingMapper.getMaxBatchId();

        // 生成新的批次号
        String newBatchId = generateBatchId(maxBatchId);
        return newBatchId;
    }

}




