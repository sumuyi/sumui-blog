package com.sumui.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class FileStorageService {
    @Resource
    private MinioClient minioClient;
    @Value("${minio.frp-endpoint}")
    private String frpEndpoint;
    @Value("${minio.bucket}")
    private String bucketName;

    // 文件上传
    public String uploadFile(MultipartFile file, String objectName) throws Exception {
        if (StrUtil.isBlank(objectName)) {
            objectName = file.getOriginalFilename();
        }

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object("/test/" + objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        return objectName;
    }

    public String getSimpleFileUrl(String objectName) throws Exception {
        if (StringUtils.hasText(frpEndpoint)) {
            return frpEndpoint + "/" + bucketName + "/" + objectName;
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
