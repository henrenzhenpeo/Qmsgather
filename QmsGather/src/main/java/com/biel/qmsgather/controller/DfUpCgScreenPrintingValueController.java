package com.biel.qmsgather.controller;

import com.biel.qmsgather.domain.upweb.DfUpCgScreenPrinting;
import com.biel.qmsgather.domain.upweb.DfUpCgScreenPrintingValue;
import com.biel.qmsgather.service.DfUpCgScreenPrintingService;
import com.biel.qmsgather.service.DfUpCgScreenPrintingValueService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cg/screenPrintingValue")
@Api(tags = "油墨密度测量值")
public class DfUpCgScreenPrintingValueController {

    @Autowired
    private DfUpCgScreenPrintingService dfUpCgScreenPrintingService;

    @Autowired
    private DfUpCgScreenPrintingValueService dfUpCgScreenPrintingValueService;

    @PostMapping("/upload")
    @ApiOperation(value = "油墨密度测量值接口上传")
    public Result uploadScreenPrintingValueInfo(@RequestBody DfUpCgScreenPrintingValue dfUpCgScreenPrintingValue){
        String newBatchId = dfUpCgScreenPrintingService.getMaxBatchId();
        System.out.println("newBatchId"+newBatchId);
        //插入批次号value表
        dfUpCgScreenPrintingValue.setBatchId(dfUpCgScreenPrintingValue.getProcess()+newBatchId);
        //插入批次号ScreenPrinting表
        for (DfUpCgScreenPrinting dfUpCgScreenPrinting : dfUpCgScreenPrintingValue.getDfUpCgScreenPrintingList()) {
            dfUpCgScreenPrinting.setBatchId(dfUpCgScreenPrintingValue.getProcess()+newBatchId);
        }
        if (dfUpCgScreenPrintingValue!=null) {
            boolean saveScreenPrinting = dfUpCgScreenPrintingService.saveBatch(dfUpCgScreenPrintingValue.getDfUpCgScreenPrintingList());
            if (saveScreenPrinting) {
                boolean saveScreenPrintingValue = dfUpCgScreenPrintingValueService.save(dfUpCgScreenPrintingValue);
                if (saveScreenPrintingValue) {
                    return new Result(200,"油墨密度测量值上传成功");
                }
                return new Result(500,"油墨密度测量值上传失败");
            }
            return new Result(500,"油墨密度测量值上传失败");
        }

        return new Result(500,"请上传油墨密度测量值");
    }
}
