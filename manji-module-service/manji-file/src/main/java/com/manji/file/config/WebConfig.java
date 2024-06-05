package com.manji.file.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private OSSConfigProperties properties;

    /**
     * 配置静态资源的访问地址
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler("/static/**");
        String localPath = properties.getLocalPath();
        resourceHandlerRegistration
                .addResourceLocations("file:" + localPath);
    }
}
