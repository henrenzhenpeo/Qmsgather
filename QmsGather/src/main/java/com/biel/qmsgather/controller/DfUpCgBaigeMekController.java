package com.biel.qmsgather.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biel.qmsgather.domain.dto.DfUpCgBaigeMekDto;
import com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek;
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


    @PostMapping("/findBaigeMek")
    @ApiOperation(value = "百格mek查询接口")
    public R findBaigeMek(@RequestBody DfUpCgBaigeMekDto dfUpCgBaigeMekDto) {
        QueryWrapper<DfUpCgBaigeMek> dfUpCgBaigeMekQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(dfUpCgBaigeMekDto.getProcess())) {
            dfUpCgBaigeMekQueryWrapper.eq("process", dfUpCgBaigeMekDto.getProcess());
        }
        if (StringUtils.isNotEmpty(dfUpCgBaigeMekDto.getFactory())) {
            dfUpCgBaigeMekQueryWrapper.eq("factory", dfUpCgBaigeMekDto.getFactory());
        }
        if (StringUtils.isNotEmpty(dfUpCgBaigeMekDto.getStage())) {
            dfUpCgBaigeMekQueryWrapper.eq("stage", dfUpCgBaigeMekDto.getStage());
        }

        if (StringUtils.isNotEmpty(dfUpCgBaigeMekDto.getStartTestDate()) && StringUtils.isNotEmpty(dfUpCgBaigeMekDto.getEndTestDate())) {
            dfUpCgBaigeMekQueryWrapper.between("test_date",dfUpCgBaigeMekDto.getStartTestDate(),dfUpCgBaigeMekDto.getEndTestDate());

        }

        IPage<DfUpCgBaigeMek> page = dfUpCgBaigeMekService.page(new Page<>(dfUpCgBaigeMekDto.getPageIndex(), dfUpCgBaigeMekDto.getPageSize()), dfUpCgBaigeMekQueryWrapper);
        return R.ok(page);
    }

}
