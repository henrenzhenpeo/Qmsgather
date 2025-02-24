package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.*;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @autor 96901
 * @date 2025/2/18
 */

@Slf4j
@RestController
@RequestMapping("/api/ink-depth")
@Api(tags = "C98B-油墨厚度")
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







    @Autowired
    private DfExcelC98bInkBm0Config01Service dfExcelC98bInkBm0Config01Service;

    @Autowired
    private DfExcelC98bInkBm1Config01Service dfExcelC98bInkBm1Config01Service;

    @Autowired
    private DfExcelC98bInkBm2Config01Service dfExcelC98bInkBm2Config01Service;

    @Autowired
    private DfExcelC98bInkBm3Config01Service dfExcelC98bInkBm3Config01Service;






    @Autowired
    private DfExcelC98bInkBm3andoilConfig01Service dfExcelC98bInkBm3andoilConfig01Service;


    @Autowired
    private DfExcelC98bInkBm3andwireandoilConfig01Service dfExcelC98bInkBm3andwireandoilConfig01Service;



    @Autowired
    private DfExcelC98bInkBm3andwireConfig01Service dfExcelC98bInkBm3andwireConfig01Service;





    @Autowired
    private DfExcelC98bInkDepthConfig01Service dfExcelC98bInkDepthConfig01Service;



    @Autowired
    private DfExcelC98bInkIr1Config01Service dfExcelC98bInkIr1Config01Service;


    @Autowired
    private DfExcelC98bInkIr2Config01Service dfExcelC98bInkIr2Config01Service;



    @Autowired
    private DfExcelC98bInkOilConfig01Service dfExcelC98bInkOilConfig01Service;




























    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new Result<>(500, "请选择要上传的文件");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return new Result<>(500, "请上传Excel文件");
            }

            // 检查文件名前缀
            String expectedPrefix = "C98B-油墨厚度";
            if (!fileName.startsWith(expectedPrefix)) {
                return new Result<>(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
            }

            // 生成批次号
            String batchId = generateBatchId(fileName);

            // 导入结果收集
            Map<String, Object> importResults = new HashMap<>();
            importResults.put("batchId", batchId);

            // BM0相关导入
            importResults.put("bm0ConfigCount", dfExcelC98bInkBm0ConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm0Data", dfExcelC98bInkBm0Service.importExcelData(file, batchId));

            // BM1相关导入
            importResults.put("bm1ConfigCount", dfExcelC98bInkBm1ConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm1Data", dfExcelC98bInkBm1Service.importExcelData(file, batchId));

            // BM2相关导入
            importResults.put("bm2ConfigCount", dfExcelC98bInkBm2ConfigService.importBm2ConfigFromExcel(file, batchId));
            importResults.put("bm2Data", dfExcelC98bInkBm2Service.importExcelData(file, batchId));

            // BM3相关导入
            importResults.put("bm3ConfigCount", dfExcelC98bInkBm3ConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm3Data", dfExcelC98bInkBm3Service.importExcelData(file, batchId));

            // BM3 and oil相关导入
            importResults.put("bm3andoilConfigCount", dfExcelC98bInkBm3andoilConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm3andoilData", dfExcelC98bInkBm3andoilService.importExcelData(file, batchId));

            // BM3 and wire and oil相关导入
            importResults.put("bm3andwireandoilConfigCount", dfExcelC98bInkBm3andwireandoilConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm3andwireandoilCount", dfExcelC98bInkBm3andwireandoilService.importExcelData(file, batchId));

            // BM3 and wire相关导入
            importResults.put("bm3andwireConfigCount", dfExcelC98bInkBm3andwireConfigService.importConfigFromExcel(file, batchId));
            importResults.put("bm3andwireData", dfExcelC98bInkBm3andwireService.importExcelData(file, batchId));

            // Depth相关导入
            importResults.put("depthConfigCount", dfExcelC98bInkDepthConfigService.importConfigFromExcel(file, batchId));
            importResults.put("depthData", dfExcelC98bInkDepthService.importExcelData(file, batchId));

            // IR1相关导入
            importResults.put("ir1ConfigData", dfExcelC98bInkIr1ConfigService.importIr1ConfigFromExcel(file, batchId));
            importResults.put("ir1Count", dfExcelC98bInkIr1Service.importFromExcel(file, batchId));

            // IR2相关导入
            importResults.put("ir2ConfigCount", dfExcelC98bInkIr2ConfigService.importConfigFromExcel(file, batchId));
            importResults.put("ir2Data", dfExcelC98bInkIr2Service.importExcelData(file, batchId));

            // Oil相关导入
            importResults.put("oilConfigCount", dfExcelC98bInkOilConfigService.importOilConfigFromExcel(file, batchId));
            importResults.put("oilCount", dfExcelC98bInkOilService.importExcelData(file, batchId));





// ... existing code ...

// BM0相关导入

            importResults.put("bm0Config01Count", dfExcelC98bInkBm0Config01Service.importConfigFromExcel(file, batchId));

// BM1相关导入

            importResults.put("bm1Config01Count", dfExcelC98bInkBm1Config01Service.importConfigFromExcel(file, batchId));

// BM2相关导入

            importResults.put("bm2Config01Count", dfExcelC98bInkBm2Config01Service.importConfigFromExcel(file, batchId));

// BM3相关导入

            importResults.put("bm3Config01Count", dfExcelC98bInkBm3Config01Service.importConfigFromExcel(file, batchId));

// BM3 and oil相关导入

            importResults.put("bm3andoilConfig01Count", dfExcelC98bInkBm3andoilConfig01Service.importConfigFromExcel(file, batchId));

// BM3 and wire and oil相关导入

            importResults.put("bm3andwireandoilConfig01Count", dfExcelC98bInkBm3andwireandoilConfig01Service.importConfigFromExcel(file, batchId));

// BM3 and wire相关导入

            importResults.put("bm3andwireConfig01Count", dfExcelC98bInkBm3andwireConfig01Service.importConfigFromExcel(file, batchId));

// Depth相关导入

            importResults.put("depthConfig01Count", dfExcelC98bInkDepthConfig01Service.importConfigFromExcel(file, batchId));

// IR1相关导入

            importResults.put("ir1Config01Count", dfExcelC98bInkIr1Config01Service.importConfigFromExcel(file, batchId));

// IR2相关导入

            importResults.put("ir2Config01Count", dfExcelC98bInkIr2Config01Service.importConfigFromExcel(file, batchId));

// Oil相关导入

            importResults.put("oilConfig01Count", dfExcelC98bInkOilConfig01Service.importConfigFromExcel(file, batchId));

// ... existing code ...










            return new Result<>(200, "文件导入成功", importResults);

        } catch (Exception e) {
            log.error("文件导入失败", e);
            return new Result<>(500, "文件导入失败：" + e.getMessage());
        }
    }

    private String generateBatchId(String fileName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String baseFileName = fileName.toLowerCase().replaceAll("\\.(xlsx|xls)$", "");
        return baseFileName + "_" + timestamp;
    }


















    // @PostMapping("/import")
    // public ResponseEntity<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
    //     Map<String, Object> response = new HashMap<>();
    //
    //     try {
    //         if (file.isEmpty()) {
    //             response.put("success", false);
    //             response.put("message", "请选择要上传的文件");
    //             return ResponseEntity.badRequest().body(response);
    //         }
    //
    //         String fileName = file.getOriginalFilename();
    //         if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
    //             response.put("success", false);
    //             response.put("message", "请上传Excel文件");
    //             return ResponseEntity.badRequest().body(response);
    //         }
    //
    //         // 调用各个Service的导入方法
    //         int bm0ConfigCount = dfExcelC98bInkBm0ConfigService.importConfigFromExcel(file);
    //         int bm1ConfigCount = dfExcelC98bInkBm1ConfigService.importConfigFromExcel(file);
    //         int bm2ConfigCount = dfExcelC98bInkBm2ConfigService.importBm2ConfigFromExcel(file);
    //         int bm3ConfigCount = dfExcelC98bInkBm3ConfigService.importConfigFromExcel(file);
    //
    //
    //         int bm3andoilConfigCount = dfExcelC98bInkBm3andoilConfigService.importConfigFromExcel(file);
    //         Map<String, Object> bm3andoilCount = dfExcelC98bInkBm3andoilService.importExcelData(file);
    //
    //         int bm3andwireandoilConfigCount = dfExcelC98bInkBm3andwireandoilConfigService.importConfigFromExcel(file);
    //         int bm3andwireandoilCount = dfExcelC98bInkBm3andwireandoilService.importExcelData(file);
    //
    //         int bm3andwireConfigCount = dfExcelC98bInkBm3andwireConfigService.importConfigFromExcel(file);
    //         Map<String, Object>  bm3andwireCount = dfExcelC98bInkBm3andwireService.importExcelData(file);
    //
    //         int bm3ConfigCount1 = dfExcelC98bInkBm3ConfigService.importConfigFromExcel(file);
    //         int bm3Count = dfExcelC98bInkBm3Service.importExcelData(file);
    //
    //         int depthConfigCount = dfExcelC98bInkDepthConfigService.importConfigFromExcel(file);
    //         Map<String, Object> depthCount = dfExcelC98bInkDepthService.importExcelData(file);
    //
    //         Map<String, Object> ir1ConfigCount = dfExcelC98bInkIr1ConfigService.importIr1ConfigFromExcel(file);
    //         int ir1Count = dfExcelC98bInkIr1Service.importFromExcel(file);
    //
    //         int ir2ConfigCount = dfExcelC98bInkIr2ConfigService.importConfigFromExcel(file);
    //         Map<String, Object> ir2Count = dfExcelC98bInkIr2Service.importExcelData(file);
    //
    //         int oilConfigCount = dfExcelC98bInkOilConfigService.importOilConfigFromExcel(file);
    //         int oilCount = dfExcelC98bInkOilService.importExcelData(file);
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //         Map<String, Object> bm0Count = dfExcelC98bInkBm0Service.importExcelData(file);
    //         Map<String, Object> bm1 = dfExcelC98bInkBm1Service.importExcelData(file);
    //         Map<String, Object>  bm2Count = dfExcelC98bInkBm2Service.importExcelData(file);
    //         int  bm3Cou1nt = dfExcelC98bInkBm3Service.importExcelData(file);
    //
    //         // int totalCount = bm0Count + bm1Count + bm2Count + bm3Count +
    //         //         bm0ConfigCount + bm1ConfigCount + bm2ConfigCount + bm3ConfigCount;
    //
    //         response.put("success", true);
    //         response.put("message", "文件导入成功");
    //         // response.put("data", totalCount);
    //         return ResponseEntity.ok(response);
    //
    //     } catch (Exception e) {
    //         log.error("文件导入失败", e);
    //         response.put("success", false);
    //         response.put("message", "文件导入失败：" + e.getMessage());
    //         return ResponseEntity.internalServerError().body(response);
    //     }
    // }
    //












}
