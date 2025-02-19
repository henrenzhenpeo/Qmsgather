package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualConfigService;
import com.biel.qmsgather.service.DfExcelC98bRCornerGrooveService;
import com.biel.qmsgather.service.DfExcelC98bWireframe20240624ManualService;
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

/**
 * @autor 96901
 * @date 2025/2/13
 */

@Slf4j
@RestController
@RequestMapping("/api/corner-grooves")
public class DfExcelC98bWireframe20240624ManualController {


    @Autowired
    private DfExcelC98bWireframe20240624ManualService cornerGrooveService;


    @Autowired
    private DfExcelC98bLayer2Program20240624ManualConfigService layer2Program20240624ManualConfigService;




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
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "请上传Excel文件");
                return ResponseEntity.badRequest().body(response);
            }

            int successCount = cornerGrooveService.importExcelData(file);
            int ds = layer2Program20240624ManualConfigService.importConfigFromExcel(file);



            response.put("success", true);
            response.put("message", "文件导入成功");
            response.put("data", successCount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("文件导入失败", e);
            response.put("success", false);
            response.put("message", "文件导入失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


}
