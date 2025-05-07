package ru.onliver.content_manager.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.onliver.content_manager.util.MinioUrlUtil;

@Component
public class MinioInitializer {
    private final MinioConfig minioConfig;
    private final MinioUrlUtil minioUrlUtil;

    public MinioInitializer(MinioConfig minioConfig, MinioUrlUtil minioUrlUtil) {
        this.minioConfig = minioConfig;
        this.minioUrlUtil = minioUrlUtil;
    }

    @PostConstruct
    public void ensureBucketsExist() {
        minioUrlUtil.createBucketIfNotExists(minioConfig.getContentBucketName());
        minioUrlUtil.createBucketIfNotExists(minioConfig.getAvatarBucketName());
    }

}
