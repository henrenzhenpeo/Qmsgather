package com.biel.qmsgather.controller;

import com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek;
import com.biel.qmsgather.domain.upweb.DfUpCgJindoResistance;
import com.biel.qmsgather.service.DfUpCgBaigeMekService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cg/baigeMek")
@CrossOrigin
@Api(tags = "百格mek接口")
public class DfUpCgBaigeMekController {

    @Autowired
    private DfUpCgBaigeMekService dfUpCgBaigeMekService;

    @PostMapping("/upload")
    @ApiOperation(value = "百格mek接口上传")
    public Result uploadBaigeMekInfo(@RequestBody List<DfUpCgBaigeMek> dfUpCgBaigeMekList){

        String newBatchId = dfUpCgBaigeMekService.getMaxBatchId();
        System.out.println("newBatchId"+newBatchId);

        for (DfUpCgBaigeMek dfUpCgBaigeMek : dfUpCgBaigeMekList) {
            dfUpCgBaigeMek.setBatchId(newBatchId);
        }

        boolean b = dfUpCgBaigeMekService.saveBatch(dfUpCgBaigeMekList);
//        Boolean b = dfUpCgResistanceService.save(dfUpCgResistanceList)    ;
        if (b) {
            return new Result(200,"百格mek接口上传成功");
        }

        return new Result(500,"百格mek接口上传失败");
    }

}
