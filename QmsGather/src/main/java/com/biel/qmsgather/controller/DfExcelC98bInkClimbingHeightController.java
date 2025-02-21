package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bWireframeInkClimbingHeightConfigService;
import com.biel.qmsgather.service.DfExcelC98bWireframeInkClimbingHeightService;
import com.biel.qmsgather.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ink-climbing-height")
public class DfExcelC98bInkClimbingHeightController {

    @Autowired
    private DfExcelC98bWireframeInkClimbingHeightService inkClimbingHeightService;

    @Autowired
    private DfExcelC98bWireframeInkClimbingHeightConfigService configService;

    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestPart("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return new Result<>(500, "请选择要上传的文件");
        }

        // 检查文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            return new Result<>(500, "请上传Excel文件(.xlsx格式)");
        }

        // 检查文件名前缀
        String expectedPrefix = "C98B 线框爬墨高度";
        if (!fileName.startsWith(expectedPrefix)) {
            return new Result<>(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
        }

        try {
            // 生成批次号
            String batchId = generateBatchId(fileName);

            // 导入数据和配置
            int dataCount = inkClimbingHeightService.importFromExcel(file, batchId);
            int configCount = configService.importConfigFromExcel(file, batchId);

            // 封装返回结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("dataCount", dataCount);
            resultMap.put("configCount", configCount);
            resultMap.put("batchId", batchId);

            return new Result<>(200, "导入成功", resultMap);
        } catch (Exception e) {
            log.error("导入失败", e);
            return new Result<>(500, "导入失败：" + e.getMessage());
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