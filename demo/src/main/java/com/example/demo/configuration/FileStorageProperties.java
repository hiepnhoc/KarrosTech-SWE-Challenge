package com.example.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    private String supportExtension;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getSupportExtension() {
        return supportExtension;
    }

    public void setSupportExtension(String supportExtension) {
        this.supportExtension = supportExtension;
    }
}