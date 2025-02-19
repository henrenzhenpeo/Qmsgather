package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bRCornerGrooveConfigService;
import com.biel.qmsgather.service.DfExcelC98bRCornerGrooveService;
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

@Slf4j
@RestController
@RequestMapping("/api/corner-groosve")
public class DfExcelC98bRCornerGrooveController {

    @Autowired
    private DfExcelC98bRCornerGrooveService cornerGrooveService;



    @Autowired
    private DfExcelC98bRCornerGrooveConfigService cornerGrooveConfigService;





    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要上传的文件");
                return ResponseEntity.badRequest().body(response);
            }

            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                response.put("success", false);
                response.put("message", "请上传Excel文件");
                return ResponseEntity.badRequest().body(response);
            }

            int successCount = cornerGrooveService.importExcelData(file);
            int  ds = cornerGrooveConfigService.importExcelData(file);

            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("importCount", successCount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            response.put("success", false);
            response.put("message", "文件处理失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}