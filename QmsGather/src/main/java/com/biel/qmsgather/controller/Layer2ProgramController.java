package com.biel.qmsgather.controller;


import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualConfigService;
import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualService;

import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/layer2-program")
@Api(tags = "二层程序数据导入接口")
public class Layer2ProgramController {

    @Autowired
    private DfExcelC98bLayer2Program20240624ManualService layer2ProgramService;

    @Autowired
    private DfExcelC98bLayer2Program20240624ManualConfigService dfExcelC98bLayer2Program20240624ManualConfigService;


    @PostMapping("/import")
    @ApiOperation("导入Excel数据")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        try {
            layer2ProgramService.importExcelData(file);
            dfExcelC98bLayer2Program20240624ManualConfigService.importConfigFromExcel(file);

            return new Result(200,"百格mek接口上传成功");
        } catch (Exception e) {
            log.error("数据导入失败：", e);
            return new Result(500,"百格mek接口上传失败");
        }
    }
}