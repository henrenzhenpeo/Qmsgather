package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigService;
import com.biel.qmsgather.service.DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedService;
import com.biel.qmsgather.util.Result;
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

@Slf4j
@RestController
@RequestMapping("/api/qr-code-position")
public class DfExcelC98bQrCodePositionController {

    @Autowired
    private DfExcelC98bQrCodePositionHoleLeft8xZNewestFixedService qrCodePositionService;

    @Autowired
    private DfExcelC98bQrCodePositionHoleLeft8xZNewestConfigService configService;

    @PostMapping("/import")
    public Result<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> resultData = new HashMap<>();

        try {
            if (file.isEmpty()) {
                return new Result<>(500, "请选择要上传的文件");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return new Result<>(500, "请上传Excel文件（.xlsx或.xls格式）");
            }

            // 检查文件名前缀
            String expectedPrefix = "C98B二维码位置度（孔向左）8倍Z最新不动";
            if (!fileName.startsWith(expectedPrefix)) {
                return new Result<>(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
            }

            // 生成批次号
            String batchId = generateBatchId(fileName);
            log.info("开始导入文件：{}，批次号：{}", fileName, batchId);

            // 导入数据和配置
            int dataCount = qrCodePositionService.importExcelData(file, batchId);
            int configCount = configService.importConfigFromExcel(file, batchId);

            resultData.put("dataCount", dataCount);
            resultData.put("configCount", configCount);
            resultData.put("batchId", batchId);

            log.info("文件{}导入完成，成功导入数据{}条，配置{}条", fileName, dataCount, configCount);
            return new Result<>(200, "文件导入成功", resultData);

        } catch (Exception e) {
            log.error("文件导入失败", e);
            return new Result<>(500, "文件处理失败：" + e.getMessage());
        }
    }

    private String generateBatchId(String fileName) {
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        // 处理文件名(移除.xlsx或.xls后缀)
        String baseFileName = fileName.toLowerCase()
                .replace(".xlsx", "")
                .replace(".xls", "");

        // 生成批次号格式: 文件名_时间戳
        return baseFileName + "_" + timestamp;
    }
}