package com.example.shopmebe.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        addResourceHandler(registry, "user-photos");
        addResourceHandler(registry, "../category-images");
    }

    private void addResourceHandler(ResourceHandlerRegistry registry, String dirName) {
        Path path = Paths.get(dirName);
        String actualDirName = path.getFileName().toString();

        String absolutePath = path.toAbsolutePath().toString();
        System.out.println("Directory Path for " + actualDirName + ": " + absolutePath);

        registry.addResourceHandler("/" + actualDirName + "/**")
                .addResourceLocations("file:/" + absolutePath + "/");
    }
}
