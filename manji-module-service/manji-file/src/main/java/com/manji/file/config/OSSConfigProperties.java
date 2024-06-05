package com.manji.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * OSS 配置信息
 *
 * @author Bqd
 * @since 2024/6/4 1:11
 */
@Configuration
@Data
public class OSSConfigProperties {
    @Value("${file-upload.oss.enable}")
    private Boolean enable;
    @Value("${file-upload.oss.local-url}")
    private String localUrl;
    @Value("${file-upload.oss.local-path}")
    private String localPath;
    @Value("${file-upload.oss.endpoint}")
    private String endpoint;
    @Value("${file-upload.oss.region}")
    private String region;
    @Value("${file-upload.oss.access-key-id}")
    private String accessKeyId;
    @Value("${file-upload.oss.access-key-secret}")
    private String accessKeySecret;
    @Value("${file-upload.oss.bucket-name}")
    private String bucketName;

    /**
     * 获取OSS对象存储 文件访问地址
     *
     * @param fileName 文件名
     * @return 文件访问地址
     */
    public String getOSSUrl(String fileName) {
        return "https://" + bucketName + "." + endpoint.replaceAll("https://", "") + "/" + fileName;
    }

    /**
     * 获取本地文件访问地址
     *
     * @param localPath 本地文件路径
     * @return 文件访问地址
     */
    public String getLocalUrl(String savePath) {
        return localUrl + savePath.replace(File.separator, "/");
    }

}
