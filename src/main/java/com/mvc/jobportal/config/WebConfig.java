package com.mvc.jobportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String uploadDir="images";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       // WebMvcConfigurer.super.addResourceHandlers(registry);
        Path path= Paths.get(uploadDir);
        registry.addResourceHandler("/"+uploadDir+"/**")
                .addResourceLocations("file:"+path.toAbsolutePath()+"/");
    }
}
