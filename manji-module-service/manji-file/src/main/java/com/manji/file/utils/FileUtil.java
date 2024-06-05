package com.manji.file.utils;

import com.aliyun.oss.OSS;
import com.manji.base.utils.SpringContextUtil;
import com.manji.base.error.BizException;
import com.manji.file.config.OSSConfigProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * 文件处理工具类
 *
 * @author Bqd
 * @since 2024/6/4 0:24
 */
@Slf4j
public class FileUtil {
    private static final OSSConfigProperties CONFIG_PROPERTIES = SpringContextUtil.getBean(OSSConfigProperties.class);
    private static OSS OSS_CLIENT;

    static {
        try {
            OSS_CLIENT = SpringContextUtil.getBean(OSS.class);
        } catch (Exception e) {
            // 此处会出现错误，有可能是配置文件没有开启OSS存储，但是不影响使用；
            log.info("OSS未开启");
        }
    }

    /**
     * 上传文件并返回文件访问路径
     *
     * @param fileBytes 文件字节数组
     * @param fileName  文件名称
     * @param paths     文件路径
     */
    public static String uploadFile(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        Boolean enable = CONFIG_PROPERTIES.getEnable();
        if (enable) {
            log.info("上传文件：OSS对象存储");
            return uploadFileToOSS(fileBytes, fileName, paths);
        } else {
            log.info("上传文件：本地存储");
            return uploadFileToLocal(fileBytes, fileName, paths);
        }
    }


    /**
     * 将文件上传到阿里OSS对象存储
     *
     * @return 文件访问路径
     */
    private static String uploadFileToOSS(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (!path.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append('/');
                }
                sb.append(path);
            }
        }

        // 追加处理之后的文件名称
        sb.append('/');
        sb.append(secureFilename(fileName));
        String filePath = sb.toString();

        // 上传到OSS对象存储服务器
        OSS_CLIENT.putObject(CONFIG_PROPERTIES.getBucketName(), filePath, new ByteArrayInputStream(fileBytes));
        return CONFIG_PROPERTIES.getOSSUrl(filePath);
    }


    /**
     * 将文件上传本地存储
     *
     * @return 文件访问路径
     */
    private static String uploadFileToLocal(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        // 处理文件名称
        String secureFilename = secureFilename(fileName);
        Path path = Paths.get(CONFIG_PROPERTIES.getLocalPath(), String.join(File.separator, paths), secureFilename);

        // 将文件字节写入本地存储
        try {
            // 确保目录存在
            Files.createDirectories(path.getParent());

            Files.write(path, fileBytes);

            String accessPath = Paths.get(String.join(File.separator, paths), secureFilename).toString();
            // 返回文件访问路径
            return CONFIG_PROPERTIES.getLocalUrl(accessPath);
        } catch (IOException e) {
            throw new BizException("文件保存到本地失败", e);
        }
    }

    /**
     * 获取安全的文件名
     *
     * @param filename 原始文件名
     * @return 安全的文件名
     */
    private static String secureFilename(String filename) {
        int i = filename.lastIndexOf('.');
        if (i == -1) {
            log.info("上传的文件：{}没有文件拓展名称。", filename);
            throw new BizException("上传的文件没有文件拓展名称。");
        }

        String ext = filename.substring(i);
        String uniqueFilename = generateUniqueFilename() + ext;

        return uniqueFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

    /**
     * 生成唯一的文件名
     *
     * @return 唯一的文件名
     */
    private static String generateUniqueFilename() {
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(9999);
        return timestamp + "_" + randomNum;
    }
}
