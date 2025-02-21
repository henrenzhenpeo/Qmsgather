package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bUltrasonicContactAngleService;
import com.biel.qmsgather.util.Result;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
@RequestMapping("/api/ultrasonic")
public class DfExcelUltrasonicExcelController {

    @Autowired
    private DfExcelC98bUltrasonicContactAngleService importService;

    @PostMapping("/import")
    public Result<?> importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Result<>(500, "请选择要上传的文件");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            return new Result<>(500, "请上传Excel文件(.xlsx格式)");
        }

        // 检查文件名前缀
        String expectedPrefix = "C98B-超声波水滴角";
        if (!fileName.startsWith(expectedPrefix)) {
            return new Result<>(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
        }

        try {
            // 生成批次号
            String batchId = generateBatchId(fileName);

            // 导入数据
            var result = importService.importExcelFile(file, batchId);

            if ((boolean)result.get("success")) {
                return new Result<>(200, "导入成功", result);
            } else {
                return new Result<>(500, (String)result.get("message"));
            }

        } catch (Exception e) {
            log.error("导入Excel失败", e);
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