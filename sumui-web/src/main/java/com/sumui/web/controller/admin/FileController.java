package com.sumui.web.controller.admin;

import com.sumui.common.model.ReqResult;
import com.sumui.service.impl.FileStorageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理
 */
@Api("文件管理")
@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public ReqResult<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String objectName = storageService.uploadFile(file, null);
            return ReqResult.ok(objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return ReqResult.fail("文件上传失败");
        }
    }

    @GetMapping("/url")
    public ReqResult<String> getUrl(@RequestParam String objectName) {
        try {
            return ReqResult.ok(storageService.getSimpleFileUrl(objectName));
        } catch (Exception e) {
            e.printStackTrace();
            return ReqResult.fail("获取文件链接失败");
        }
    }
}
