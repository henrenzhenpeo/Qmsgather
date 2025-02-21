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

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/layer2-program")
@Api(tags = "二层程序数据导入接口")
public class DfExcelC98bLayer2ProgramController {

    @Autowired
    private DfExcelC98bLayer2Program20240624ManualService layer2ProgramService;

    @Autowired
    private DfExcelC98bLayer2Program20240624ManualConfigService configService;

    @PostMapping("/import")
    @ApiOperation("导入Excel数据")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return new Result(500, "请选择要上传的文件");
            }

            // 检查文件格式
            String fileName = file.getOriginalFilename();
            // if (fileName == null || !fileName.endsWith(".xlsx")) {
            //     return new Result(500, "请上传Excel文件(.xlsx格式)");
            // }

            // 检查文件名前缀
            String expectedPrefix = "C98B-二层程序20240624(手动)";
            if (!fileName.startsWith(expectedPrefix)) {
                return new Result(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
            }

            // 生成批次号
            String batchId = generateBatchId(fileName);

            // 导入数据和配置
            layer2ProgramService.importExcelData(file, batchId);
            int configCount = configService.importConfigFromExcel(file, batchId);

            return new Result(200, "数据导入成功",
                    String.format("成功导入数据，配置导入数量: %d", configCount));

        } catch (Exception e) {
            log.error("数据导入失败：", e);
            return new Result(500, "数据导入失败：" + e.getMessage());
        }
    }

    private String generateBatchId(String fileName) {
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        // 处理文件名(移除.xlsx后缀)
        String baseFileName = fileName.toLowerCase().replace(".xlsx", "");

        // 生成批次号格式: 文件名_时间戳
        return baseFileName + "_" + timestamp;
    }
}