package com.manji.file.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云的对象存储配置
 *
 * @author Bqd
 * @since 2024/6/4 0:43
 */
@Configuration
@Slf4j
public class OSSConfig {

    @Resource
    private OSSConfigProperties properties;

    @Bean
    public OSS createOSSClient() {
        if (Boolean.FALSE.equals(properties.getEnable())) {
            log.info("未开启OSS云对象存储，跳过装配阿里云OSS");
            return null;
        }
        String endpoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();
        String region = properties.getRegion();
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(new DefaultCredentialProvider(accessKeyId, accessKeySecret))
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

}
