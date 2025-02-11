package com.biel.qmsgather.controller;

import com.biel.qmsgather.domain.upweb.DfUpCgOrtBaigeTest;
import com.biel.qmsgather.domain.upweb.DfUpCgResistance;
import com.biel.qmsgather.service.DfUpCgOrtBaigeTestService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cg/ortBaigeTest")
@CrossOrigin
@Api(tags = "百格测试")
public class DfUpCgOrtBaigeTestController {


    @Autowired
    private DfUpCgOrtBaigeTestService dfUpCgOrtBaigeTestService;

    @PostMapping("/upload")
    @ApiOperation(value = "百格测试接口上传")
    public Result uploadOrtBaigeTest(@RequestBody List<DfUpCgOrtBaigeTest> dfUpCgResistanceList){

        String newBatchId = dfUpCgOrtBaigeTestService.getMaxBatchId();
        System.out.println("newBatchId"+newBatchId);

        for (DfUpCgOrtBaigeTest dfUpCgOrtBaigeTest : dfUpCgResistanceList) {
            dfUpCgOrtBaigeTest.setBatchId(dfUpCgOrtBaigeTest.getProcess()+newBatchId);
        }
        boolean b = dfUpCgOrtBaigeTestService.saveBatch(dfUpCgResistanceList);
//        Boolean b = dfUpCgResistanceService.save(dfUpCgResistanceList);
        if (b) {
            return new Result(200,"百格测试接口上传成功");
        }

        return new Result(500,"百格测试接口上传失败");
    }

}
