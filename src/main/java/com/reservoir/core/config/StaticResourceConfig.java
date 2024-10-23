package com.reservoir.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置 /images 路径对应 uploads/images 文件夹
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/");

        // 配置 /files 路径对应 uploads/files 文件夹
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:uploads/files/");
    }
}