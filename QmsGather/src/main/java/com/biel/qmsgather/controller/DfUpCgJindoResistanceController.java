package com.biel.qmsgather.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biel.qmsgather.domain.dto.DfUpCgBaigeMekDto;
import com.biel.qmsgather.domain.dto.DfUpCgJindoResistanceDto;
import com.biel.qmsgather.domain.upweb.DfUpCgBaigeMek;
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


    @PostMapping("/findJindoResistance")
    @ApiOperation(value = "IR油电阻+零层电阻查询接口")
    public R findJindoResistance(@RequestBody DfUpCgJindoResistanceDto dfUpCgJindoResistanceDto) {
        QueryWrapper<DfUpCgJindoResistance> dfUpCgJindoResistanceQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(dfUpCgJindoResistanceDto.getProject())) {
            dfUpCgJindoResistanceQueryWrapper.eq("project", dfUpCgJindoResistanceDto.getProject());
        }
        if (StringUtils.isNotEmpty(dfUpCgJindoResistanceDto.getFactory())) {
            dfUpCgJindoResistanceQueryWrapper.eq("factory", dfUpCgJindoResistanceDto.getFactory());
        }
        if (StringUtils.isNotEmpty(dfUpCgJindoResistanceDto.getStage())) {
            dfUpCgJindoResistanceQueryWrapper.eq("stage", dfUpCgJindoResistanceDto.getStage());
        }

        if (StringUtils.isNotEmpty(dfUpCgJindoResistanceDto.getStartTestDate()) && StringUtils.isNotEmpty(dfUpCgJindoResistanceDto.getEndTestDate())) {
            dfUpCgJindoResistanceQueryWrapper.between("test_date",dfUpCgJindoResistanceDto.getStartTestDate(),dfUpCgJindoResistanceDto.getEndTestDate());

        }

        IPage<DfUpCgJindoResistance> page = dfUpCgJindoResistanceService.page(new Page<>(dfUpCgJindoResistanceDto.getPageIndex(), dfUpCgJindoResistanceDto.getPageSize()), dfUpCgJindoResistanceQueryWrapper);
        return R.ok(page);
    }

}
