package com.biel.qmsgather.controller;


import com.biel.qmsgather.domain.upweb.DfUpCgResistance;
import com.biel.qmsgather.service.DfUpCgResistanceService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *油墨密度
 */
@RestController
@RequestMapping("/cg/resistance")
@Api(tags = "边缘电阻接口")
public class DfUpCgResistanceController {

    @Autowired
    private DfUpCgResistanceService dfUpCgResistanceService;

    @PostMapping("/upload")
    @ApiOperation(value = "边缘电阻接口上传")
    public Result uploadResistanceInfo(@RequestBody List<DfUpCgResistance> dfUpCgResistanceList){

        String newBatchId = dfUpCgResistanceService.getMaxBatchId();
        System.out.println("newBatchId"+newBatchId);

        for (DfUpCgResistance dfUpCgResistance : dfUpCgResistanceList) {
            dfUpCgResistance.setBatchId(newBatchId);
        }
        boolean b = dfUpCgResistanceService.saveBatch(dfUpCgResistanceList);
//        Boolean b = dfUpCgResistanceService.save(dfUpCgResistanceList);
        if (b) {
            return new Result(200,"边缘电阻数据上传成功");
        }

        return new Result(500,"边缘电阻数据上传失败");
    }


}
