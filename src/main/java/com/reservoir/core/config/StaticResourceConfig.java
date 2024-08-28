package com.reservoir.core.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Tag(name = "静态文件服务")
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    @Operation(summary = "server:port/images/filename,拿到文件")
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:files/"); // 代理到当前项目的基础目录下的files文件夹
    }
}