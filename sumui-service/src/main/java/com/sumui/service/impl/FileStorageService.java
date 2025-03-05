package com.sumui.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.sumui.service.impl.system.SysUserService;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileStorageService {
    @Resource
    private MinioClient minioClient;
    @Value("${minio.frp-endpoint}")
    private String frpEndpoint;
    @Value("${minio.bucket}")
    private String bucketName;
    @Resource
    private SysUserService sysUserService;

    public String uploadAvatar(MultipartFile file, String userId, String type) throws Exception {
        log.error("userId = {}, type = {}", userId, type);
        if (!StrUtil.equals(StpUtil.getLoginIdAsString(), userId)) {
            log.error("userId = {}, loginId = {}", userId, StpUtil.getLoginIdAsString());
            throw new Exception("请重新登录！");
        }
        // 设置存储路径和文件名
        String filePath = "/avatar/" + userId;
        String fileName = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") +
                Objects.requireNonNull(file.getOriginalFilename())
                        .substring(file.getOriginalFilename().lastIndexOf("."));
        // 上传头像
        String fileUrl = uploadFile(file, filePath, fileName);
        if (StrUtil.isNotBlank(fileUrl) && fileUrl.startsWith(frpEndpoint)) {
            // 更新用户头像
            sysUserService.updateUserAvatar(userId, filePath, fileName);
        }
        return fileUrl;
    }

    // 文件上传
    public String uploadFile(MultipartFile file, String filePath, String objectName) throws Exception {
        if (StrUtil.isBlank(objectName)) {
            objectName = file.getOriginalFilename();
        }
        if (StrUtil.isBlank(filePath)) {
            filePath = "/tmp";
        }

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath + "/" + objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        return getSimpleFileUrl(filePath + "/" + objectName);
    }

    public String getSimpleFileUrl(String fileAllPathName) throws Exception {
        if (StringUtils.hasText(frpEndpoint)) {
            return frpEndpoint + bucketName  + fileAllPathName;
        }
        return null;
    }

    // 获取文件访问URL
    public String getFileUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(7, TimeUnit.DAYS)
                .build()
        );
    }
}
