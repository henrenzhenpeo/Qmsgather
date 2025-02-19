package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.C98bBevelDataService;
import com.biel.qmsgather.service.DfExcelC98bBevelChamferMetricsConfigService;
import com.biel.qmsgather.service.DfExcelC98bBevelChamferMetricsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bevel-data")
@Slf4j
public class C98bBevelDataController {

    @Autowired
    private DfExcelC98bBevelChamferMetricsDataService c98bBevelDataService;



    @Autowired
    private DfExcelC98bBevelChamferMetricsConfigService dfExcelC98bBevelChamferMetricsConfigService;






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

        try {
            Map<String, Object> result = c98bBevelDataService.importExcelData(file);

            int ds = dfExcelC98bBevelChamferMetricsConfigService.importConfigFromExcel(file);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("导入Excel数据失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "导入失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}