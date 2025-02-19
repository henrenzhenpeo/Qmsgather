package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.*;
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
 * @date 2025/2/18
 */

@Slf4j
@RestController
@RequestMapping("/api/corners-grooves")
public class DfInkDepthController {


    @Autowired
    private DfExcelC98bInkBm0ConfigService dfExcelC98bInkBm0ConfigService;

    @Autowired
    private DfExcelC98bInkBm1ConfigService dfExcelC98bInkBm1ConfigService;

    @Autowired
    private DfExcelC98bInkBm2ConfigService dfExcelC98bInkBm2ConfigService;

    @Autowired
    private DfExcelC98bInkBm3ConfigService dfExcelC98bInkBm3ConfigService;

    @Autowired
    private DfExcelC98bInkBm0Service dfExcelC98bInkBm0Service;

    @Autowired
    private DfExcelC98bInkBm1Service dfExcelC98bInkBm1Service;


    @Autowired
    private DfExcelC98bInkBm2Service dfExcelC98bInkBm2Service;


    @Autowired
    private DfExcelC98bInkBm3Service dfExcelC98bInkBm3Service;


    @Autowired
    private DfExcelC98bInkBm3andoilConfigService dfExcelC98bInkBm3andoilConfigService;

    @Autowired
    private DfExcelC98bInkBm3andoilService dfExcelC98bInkBm3andoilService;

    @Autowired
    private DfExcelC98bInkBm3andwireandoilConfigService dfExcelC98bInkBm3andwireandoilConfigService;

    @Autowired
    private DfExcelC98bInkBm3andwireandoilService dfExcelC98bInkBm3andwireandoilService;

    @Autowired
    private DfExcelC98bInkBm3andwireConfigService dfExcelC98bInkBm3andwireConfigService;

    @Autowired
    private DfExcelC98bInkBm3andwireService dfExcelC98bInkBm3andwireService;



    @Autowired
    private DfExcelC98bInkDepthConfigService dfExcelC98bInkDepthConfigService;

    @Autowired
    private DfExcelC98bInkDepthService dfExcelC98bInkDepthService;

    @Autowired
    private DfExcelC98bInkIr1ConfigService dfExcelC98bInkIr1ConfigService;

    @Autowired
    private DfExcelC98bInkIr1Service dfExcelC98bInkIr1Service;

    @Autowired
    private DfExcelC98bInkIr2ConfigService dfExcelC98bInkIr2ConfigService;

    @Autowired
    private DfExcelC98bInkIr2Service dfExcelC98bInkIr2Service;

    @Autowired
    private DfExcelC98bInkOilConfigService dfExcelC98bInkOilConfigService;

    @Autowired
    private DfExcelC98bInkOilService dfExcelC98bInkOilService;

























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

            // 调用各个Service的导入方法
            int bm0ConfigCount = dfExcelC98bInkBm0ConfigService.importConfigFromExcel(file);
            int bm1ConfigCount = dfExcelC98bInkBm1ConfigService.importConfigFromExcel(file);
            int bm2ConfigCount = dfExcelC98bInkBm2ConfigService.importBm2ConfigFromExcel(file);
            int bm3ConfigCount = dfExcelC98bInkBm3ConfigService.importConfigFromExcel(file);


            int bm3andoilConfigCount = dfExcelC98bInkBm3andoilConfigService.importConfigFromExcel(file);
            Map<String, Object> bm3andoilCount = dfExcelC98bInkBm3andoilService.importExcelData(file);

            int bm3andwireandoilConfigCount = dfExcelC98bInkBm3andwireandoilConfigService.importConfigFromExcel(file);
            int bm3andwireandoilCount = dfExcelC98bInkBm3andwireandoilService.importExcelData(file);

            int bm3andwireConfigCount = dfExcelC98bInkBm3andwireConfigService.importConfigFromExcel(file);
            Map<String, Object>  bm3andwireCount = dfExcelC98bInkBm3andwireService.importExcelData(file);

            int bm3ConfigCount1 = dfExcelC98bInkBm3ConfigService.importConfigFromExcel(file);
            int bm3Count = dfExcelC98bInkBm3Service.importExcelData(file);

            int depthConfigCount = dfExcelC98bInkDepthConfigService.importConfigFromExcel(file);
            Map<String, Object> depthCount = dfExcelC98bInkDepthService.importExcelData(file);

            Map<String, Object> ir1ConfigCount = dfExcelC98bInkIr1ConfigService.importIr1ConfigFromExcel(file);
            int ir1Count = dfExcelC98bInkIr1Service.importFromExcel(file);

            int ir2ConfigCount = dfExcelC98bInkIr2ConfigService.importConfigFromExcel(file);
            Map<String, Object> ir2Count = dfExcelC98bInkIr2Service.importExcelData(file);

            int oilConfigCount = dfExcelC98bInkOilConfigService.importOilConfigFromExcel(file);
            int oilCount = dfExcelC98bInkOilService.importExcelData(file);















            Map<String, Object> bm0Count = dfExcelC98bInkBm0Service.importExcelData(file);
            Map<String, Object> bm1 = dfExcelC98bInkBm1Service.importExcelData(file);
            Map<String, Object>  bm2Count = dfExcelC98bInkBm2Service.importExcelData(file);
            int  bm3Cou1nt = dfExcelC98bInkBm3Service.importExcelData(file);

            // int totalCount = bm0Count + bm1Count + bm2Count + bm3Count +
            //         bm0ConfigCount + bm1ConfigCount + bm2ConfigCount + bm3ConfigCount;

            response.put("success", true);
            response.put("message", "文件导入成功");
            // response.put("data", totalCount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("文件导入失败", e);
            response.put("success", false);
            response.put("message", "文件导入失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }













}
