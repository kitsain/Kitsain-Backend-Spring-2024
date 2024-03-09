package com.myblogbackend.blog.config.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String endpoint;
    private Integer port;
    private String accessKey;
    private String secretKey;
    private boolean secure;
    private String bucketName;
    private long imageSize;
    private long fileSize;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(endpoint, port, secure)
                .build();
    }
}
