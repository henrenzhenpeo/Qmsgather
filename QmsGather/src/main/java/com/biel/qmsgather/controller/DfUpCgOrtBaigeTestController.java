package com.biel.qmsgather.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biel.qmsgather.domain.dto.DfUpCgJindoResistanceDto;
import com.biel.qmsgather.domain.dto.DfUpCgOrtBaigeTestDto;
import com.biel.qmsgather.domain.upweb.DfUpCgJindoResistance;
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


    @PostMapping("/findJindoResistance")
    @ApiOperation(value = "IR油电阻+零层电阻查询接口")
    public R findJindoResistance(@RequestBody DfUpCgOrtBaigeTestDto dfUpCgOrtBaigeTestDto) {
        QueryWrapper<DfUpCgOrtBaigeTest> dfUpCgOrtBaigeTestQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(dfUpCgOrtBaigeTestDto.getProject())) {
            dfUpCgOrtBaigeTestQueryWrapper.eq("project", dfUpCgOrtBaigeTestDto.getProject());
        }
        if (StringUtils.isNotEmpty(dfUpCgOrtBaigeTestDto.getFactory())) {
            dfUpCgOrtBaigeTestQueryWrapper.eq("factory", dfUpCgOrtBaigeTestDto.getFactory());
        }
        if (StringUtils.isNotEmpty(dfUpCgOrtBaigeTestDto.getStage())) {
            dfUpCgOrtBaigeTestQueryWrapper.eq("stage", dfUpCgOrtBaigeTestDto.getStage());
        }

        if (StringUtils.isNotEmpty(dfUpCgOrtBaigeTestDto.getStartTestDate()) && StringUtils.isNotEmpty(dfUpCgOrtBaigeTestDto.getEndTestDate())) {
            dfUpCgOrtBaigeTestQueryWrapper.between("test_date",dfUpCgOrtBaigeTestDto.getStartTestDate(),dfUpCgOrtBaigeTestDto.getEndTestDate());

        }

        IPage<DfUpCgOrtBaigeTest> page = dfUpCgOrtBaigeTestService.page(new Page<>(dfUpCgOrtBaigeTestDto.getPageIndex(), dfUpCgOrtBaigeTestDto.getPageSize()), dfUpCgOrtBaigeTestQueryWrapper);
        return R.ok(page);
    }

}
