package com.biel.qmsgather.controller;

import com.biel.qmsgather.service.DfExcelC98bLayer2Program20240624ManualConfigService;
import com.biel.qmsgather.service.DfYfExcelSandblastingUploadTemplateDataService;
import com.biel.qmsgather.util.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @autor 96901
 * @date 2025/2/25
 */


@RestController
@RequestMapping("/api/sandblastingupload")
@Slf4j
public class DfYfExcelSandblastingUploadTemplateDataController {


    @Autowired
    private DfYfExcelSandblastingUploadTemplateDataService dfYfExcelSandblastingUploadTemplateDataService;

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
             if (fileName == null || !fileName.endsWith(".xlsx")) {
                return new Result(500, "请上传Excel文件(.xlsx格式)");
             }

            // 检查文件名前缀
            String expectedPrefix = "喷砂上传模版";
            if (!fileName.startsWith(expectedPrefix)) {
                return new Result(500, "文件名格式错误，应以 '" + expectedPrefix + "' 开头");
            }



            // 导入数据和配置
         int configCount=  dfYfExcelSandblastingUploadTemplateDataService.importDataFromExcel(file);


            return new Result(200, "数据导入成功",
                    String.format("成功导入数据，配置导入数量: %d", configCount));

        } catch (Exception e) {
            log.error("数据导入失败：", e);
            return new Result(500, "数据导入失败：" + e.getMessage());
        }
    }








}
