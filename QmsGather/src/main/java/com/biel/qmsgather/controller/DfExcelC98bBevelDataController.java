package com.biel.qmsgather.controller;


import com.biel.qmsgather.service.DfExcelC98bBevelChamferMetricsConfigService;
import com.biel.qmsgather.service.DfExcelC98bBevelChamferMetricsDataService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bevel-data")
@Slf4j
@Api(tags = "C98B斜边数据导入接口", description = "处理C98B斜边和倒角数据的Excel导入")
public class DfExcelC98bBevelDataController {

    @Autowired
    private DfExcelC98bBevelChamferMetricsDataService c98bBevelDataService;

    @Autowired
    private DfExcelC98bBevelChamferMetricsConfigService configService;

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importExcelData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "请选择要上传的文件");
            return ResponseEntity.badRequest().body(response);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "请上传Excel文件(.xlsx格式)");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查文件名前缀
        String expectedPrefix = "C98B 精雕斜边+倒角- 9836-0.5 11-4";
        if (!fileName.startsWith(expectedPrefix)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 生成批次号
            String batchId = generateBatchId(fileName);

            // 导入数据和配置
            Map<String, Object> result = c98bBevelDataService.importExcelData(file, batchId);
            int configCount = configService.importConfigFromExcel(file, batchId);

            // 合并结果
            result.put("configCount", configCount);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("导入Excel数据失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "导入失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
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