package ru.onliver.content_manager.util;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MinioUrlUtil {
    private final MinioClient minioClient;
    private final String contentBucket;
    private final String avatarBucket;

    public MinioUrlUtil(MinioClient minioClient, String contentBucket, String avatarBucket) {
        this.minioClient = minioClient;
        this.contentBucket = contentBucket;
        this.avatarBucket = avatarBucket;
    }

    public String getContentUrl(String objName){
        return getObjectUrl(objName, contentBucket);
    }

    @Cacheable(value = "avatars", key = "#objName", unless = "#result.isEmpty()")
    public String getAvatarUrl(String objName){
        log.debug("Generating avatar URL for object: {}", objName);
        String url = getObjectUrl(objName, avatarBucket);
        if (!url.isEmpty()) {
            log.debug("Avatar URL generated and cached for object: {}", objName);
        } else {
            log.warn("Failed to generate avatar URL for object: {}", objName);
        }
        return url;
    }

    public String getAvatarUploadUrl(String objName) {
        return getUploadUrl(objName, avatarBucket);
    }

    public String getContentUploadUrl(String objName) {
        return getUploadUrl(objName, contentBucket);
    }

    private String getObjectUrl(String objName, String bucket){
        ignoreSecure();
        String presignedUrl;
        try {
            presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objName)
                            .expiry(5, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
        return presignedUrl;
    }

    private String getUploadUrl(String objName, String bucket) {
        ignoreSecure();
        String presignedUrl;
        try {
            presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(objName)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
        return presignedUrl;
    }

    public void createBucketIfNotExists(String bucketName) {
        ignoreSecure();
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                System.out.println("Bucket created: " + bucketName);
            } else {
                System.out.println("Bucket already exists: " + bucketName);
            }
        } catch (Exception e) {
            // логируем и при необходимости пробрасываем или обрабатываем
            System.err.println("Error checking/creating bucket '" + bucketName + "': " + e.getMessage());
        }
    }

    public void ignoreSecure(){
        try {
            minioClient.ignoreCertCheck();
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "avatars", key = "#objName")
    public void evictAvatarCache(String objName) {
        log.debug("Evicting avatar cache for object: {}", objName);
    }

    @CacheEvict(value = "avatars", allEntries = true)
    public void evictAllAvatarCache() {
        log.debug("Evicting all avatar cache entries");
    }
}
