package com.manji.file.utils;

import com.aliyun.oss.OSS;
import com.manji.base.utils.SpringContextUtil;
import com.manji.base.error.BizException;
import com.manji.file.config.OSSConfigProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

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
     * 上传MultipartFile文件并返回文件访问路径
     *
     * @param file  MultipartFile文件
     * @param paths 文件路径
     * @return 文件访问路径
     * @throws BizException 业务异常
     */
    public static String uploadFile(MultipartFile file, @NonNull String... paths) {
        if (file == null || file.isEmpty()) {
            throw new BizException("上传的文件不能为空");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new BizException("文件名不能为空");
            }
            return uploadFile(file.getBytes(), originalFilename, paths);
        } catch (IOException e) {
            log.error("获取文件字节数据失败", e);
            throw new BizException("处理上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 上传文件并返回文件访问路径
     *
     * @param fileBytes 文件字节数组
     * @param fileName  文件名称
     * @param paths     文件路径
     * @return 文件访问路径
     * @throws BizException 业务异常
     */
    public static String uploadFile(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new BizException("上传的文件内容不能为空");
        }

        try {
            Boolean enable = CONFIG_PROPERTIES.getEnable();
            if (enable != null && enable) {
                log.info("上传文件：OSS对象存储");
                return uploadFileToOSS(fileBytes, fileName, paths);
            } else {
                log.info("上传文件：本地存储");
                return uploadFileToLocal(fileBytes, fileName, paths);
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BizException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 将文件上传到阿里OSS对象存储
     *
     * @param fileBytes 文件字节数组
     * @param fileName  文件名称
     * @param paths     文件路径
     * @return 文件访问路径
     * @throws BizException 业务异常
     */
    private static String uploadFileToOSS(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        if (OSS_CLIENT == null) {
            throw new BizException("OSS客户端未初始化，请检查配置");
        }

        String filePath = buildFilePath(fileName, paths);

        try {
            // 上传到OSS对象存储服务器
            OSS_CLIENT.putObject(CONFIG_PROPERTIES.getBucketName(), filePath, new ByteArrayInputStream(fileBytes));
            return CONFIG_PROPERTIES.getOSSUrl(filePath);
        } catch (Exception e) {
            log.error("上传文件到OSS失败", e);
            throw new BizException("上传文件到OSS失败: " + e.getMessage());
        }
    }

    /**
     * 将文件上传本地存储
     *
     * @param fileBytes 文件字节数组
     * @param fileName  文件名称
     * @param paths     文件路径
     * @return 文件访问路径
     * @throws BizException 业务异常
     */
    private static String uploadFileToLocal(byte[] fileBytes, @NonNull String fileName, @NonNull String... paths) {
        // 处理文件名称
        String secureFilename = secureFilename(fileName);
        
        // 构建完整目录路径
        String directoryPath = String.join(File.separator, paths);
        Path fullPath = Paths.get(CONFIG_PROPERTIES.getLocalPath(), directoryPath);
        Path filePath = fullPath.resolve(secureFilename);

        // 将文件字节写入本地存储
        try {
            // 确保目录存在
            Files.createDirectories(fullPath);
            Files.write(filePath, fileBytes);

            // 构建访问路径，统一使用正斜杠，避免平台差异
            String accessPath = directoryPath.replace(File.separator, "/") + "/" + secureFilename;
            
            // 返回文件访问路径
            return CONFIG_PROPERTIES.getLocalUrl(accessPath);
        } catch (IOException e) {
            log.error("文件保存到本地失败", e);
            throw new BizException("文件保存到本地失败: " + e.getMessage());
        }
    }

    /**
     * 构建文件路径
     *
     * @param fileName 文件名
     * @param paths    路径数组
     * @return 构建后的文件路径
     */
    private static String buildFilePath(String fileName, String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (path != null && !path.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append(File.separator);
                }
                sb.append(path);
            }
        }

        // 追加处理之后的文件名称
        if (!sb.isEmpty()) {
            sb.append(File.separator);
        }
        sb.append(secureFilename(fileName));
        
        return sb.toString();
    }

    /**
     * 获取安全的文件名
     *
     * @param filename 原始文件名
     * @return 安全的文件名
     * @throws BizException 业务异常
     */
    private static String secureFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new BizException("文件名称不能为空");
        }

        int i = filename.lastIndexOf('.');
        if (i == -1) {
            log.info("上传的文件：{}没有文件拓展名称。", filename);
            throw new BizException("上传的文件没有文件拓展名称");
        }

        String ext = filename.substring(i).toLowerCase();
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
