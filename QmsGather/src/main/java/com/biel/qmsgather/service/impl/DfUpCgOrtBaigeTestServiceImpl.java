package com.biel.qmsgather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biel.qmsgather.domain.upweb.DfUpCgOrtBaigeTest;
import com.biel.qmsgather.mapper.DfUpCgScreenPrintingMapper;
import com.biel.qmsgather.service.DfUpCgOrtBaigeTestService;
import com.biel.qmsgather.mapper.DfUpCgOrtBaigeTestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author dafenqi
* @description 针对表【df_up_cg_ort_baige_test(千百格测试)】的数据库操作Service实现
* @createDate 2024-12-09 15:59:43
*/
@Service
@Transactional
public class DfUpCgOrtBaigeTestServiceImpl extends ServiceImpl<DfUpCgOrtBaigeTestMapper, DfUpCgOrtBaigeTest>
    implements DfUpCgOrtBaigeTestService{
    @Resource
    private DfUpCgOrtBaigeTestMapper dfUpCgOrtBaigeTestMapper;

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
        String maxBatchId = dfUpCgOrtBaigeTestMapper.getMaxBatchId();

        // 生成新的批次号
        String newBatchId = generateBatchId(maxBatchId);
        return newBatchId;
    }
}



