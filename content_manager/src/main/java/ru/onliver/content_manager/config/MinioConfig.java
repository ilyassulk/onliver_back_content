package ru.onliver.content_manager.config;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.onliver.content_manager.util.MinioUrlUtil;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name.content}")
    @Getter
    private String contentBucketName;

    @Value("${minio.bucket-name.avatar}")
    @Getter
    private String avatarBucketName;

    @Bean
    public MinioClient minioClient(){
        return   MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public MinioUrlUtil minioUrlUtil(MinioClient minioClient){
        return new MinioUrlUtil(minioClient, contentBucketName, avatarBucketName);
    }



}
