package com.sumui.web.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.sumui.common.model.ReqResult;
import com.sumui.service.impl.FileStorageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件管理
 */
@Api("文件管理")
@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileController {
    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload/avatar")
    public ReqResult<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "userId", required = false) String userId,
                                         @RequestParam(value = "type", required = false) String type) {
        try {
            String fileUrl = storageService.uploadAvatar(file, userId, type);
            return ReqResult.ok(fileUrl);
        } catch (IOException e) {
            return ReqResult.fail("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload")
    public ReqResult<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String objectName = storageService.uploadFile(file, "", "");
            return ReqResult.ok(objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return ReqResult.fail("文件上传失败");
        }
    }

    /**
     * 上传图片文件到MinIO
     * 专门用于处理前端（如HahLife应用）上传的图片
     */
    @PostMapping("/upload/image")
    public ReqResult<String> uploadImage(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "type", required = false, defaultValue = "book") String type) {
        try {
            // 根据类型设置不同的存储路径
            String filePath = "/" + type + "/covers/";
            String fileName = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") +
                    Objects.requireNonNull(file.getOriginalFilename())
                            .substring(file.getOriginalFilename().lastIndexOf("."));

            String fileUrl = storageService.uploadFile(file, filePath + StpUtil.getLoginIdAsString(), fileName);
            return ReqResult.ok(fileUrl);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return ReqResult.fail("图片上传失败: " + e.getMessage());
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
