package com.sumui.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.sumui.service.impl.system.SysUserService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    /**
     * 从远程 URL 下载图片并上传到 MinIO
     * @param coverImage 远程图片 URL
     * @param objectName 存储对象名称（如 "images/photo.jpg"）
     */
    public String uploadFileByUrl(String coverImage, String objectName) {
        if (StrUtil.isBlank(coverImage)) {
            throw new IllegalArgumentException("图片URL不能为空");
        }

        // 验证URL格式
        try {
            URL url = new URL(coverImage);
            String protocol = url.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                throw new IllegalArgumentException("不支持的URL协议：" + protocol);
            }

            // 从远程URL获取输入流
            try (InputStream inputStream = url.openStream()) {
                // 确保存储桶存在
                if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }

                // 上传到MinIO
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .build()
                );
                return getSimpleFileUrl(objectName);
            } catch (IOException e) {
                log.error("从URL下载图片失败: {}", coverImage, e);
                throw new RuntimeException("从URL下载图片失败: " + e.getMessage());
            } catch (Exception e) {
                log.error("上传图片到MinIO失败", e);
                throw new RuntimeException("上传图片到MinIO失败: " + e.getMessage());
            }
        } catch (MalformedURLException e) {
            log.error("无效的URL格式: {}", coverImage, e);
            throw new IllegalArgumentException("无效的URL格式: " + e.getMessage());
        }
    }
    
    /**
     * 上传本地图片文件到MinIO
     * @param localFilePath 本地图片文件路径
     * @return 上传后的文件URL
     */
    public String uploadLocalImage(String localFilePath) {
        if (StrUtil.isBlank(localFilePath)) {
            throw new IllegalArgumentException("本地图片路径不能为空");
        }
        
        try {
            // 获取文件名
            Path path = Paths.get(localFilePath);
            String fileName = path.getFileName().toString();
            
            // 设置存储路径
            String filePath = "/books/covers/";
            String objectName = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + "_" + fileName;
            
            // 读取文件
            File file = new File(localFilePath);
            if (!file.exists() || !file.isFile()) {
                throw new IllegalArgumentException("文件不存在或不是有效文件");
            }
            
            // 获取文件类型
            String contentType = Files.probeContentType(path);
            if (contentType == null || !contentType.startsWith("image/")) {
                contentType = "image/jpeg"; // 默认图片类型
            }
            
            // 确保存储桶存在
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            
            // 上传到MinIO
            try (InputStream inputStream = new FileInputStream(file)) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath + objectName)
                        .stream(inputStream, file.length(), -1)
                        .contentType(contentType)
                        .build()
                );
                return getSimpleFileUrl(filePath + objectName);
            }
        } catch (IOException e) {
            log.error("读取本地图片失败: {}", localFilePath, e);
            throw new RuntimeException("读取本地图片失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("上传图片到MinIO失败", e);
            throw new RuntimeException("上传图片到MinIO失败: " + e.getMessage());
        }
    }
}
