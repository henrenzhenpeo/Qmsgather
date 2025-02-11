package com.biel.qmsgather.controller;



import com.biel.qmsgather.domain.upweb.DfUpCgJindoResistance;
import com.biel.qmsgather.service.DfUpCgJindoResistanceService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cg/jindoResistance")
@CrossOrigin
@Api(tags = "IR油电阻+零层电阻接口")
public class DfUpCgJindoResistanceController {

    @Autowired
    private DfUpCgJindoResistanceService dfUpCgJindoResistanceService;


    @PostMapping("/upload")
    @ApiOperation(value = "IR油电阻+零层电阻接口上传")
    public Result uploadResistanceInfo(@RequestBody List<DfUpCgJindoResistance> dfUpCgJindoResistanceList){

        String newBatchId = dfUpCgJindoResistanceService.getMaxBatchId();
        System.out.println("newBatchId"+newBatchId);

        for (DfUpCgJindoResistance dfUpCgJindoResistance : dfUpCgJindoResistanceList) {
            dfUpCgJindoResistance.setBatchId(newBatchId);
        }
        boolean b = dfUpCgJindoResistanceService.saveBatch(dfUpCgJindoResistanceList);
//        Boolean b = dfUpCgResistanceService.save(dfUpCgResistanceList);
        if (b) {
            return new Result(200,"IR油电阻+零层电阻数据上传成功");
        }

        return new Result(500,"IR油电阻+零层电阻数据上传失败");
    }


}
