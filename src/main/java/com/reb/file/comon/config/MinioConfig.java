package com.reb.file.comon.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        // Create bucket if not exists
        try {
            BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build();

            boolean isExist = minioClient.bucketExists(existsArgs);
            if (!isExist) {
                try {
                    MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                            .bucket(bucket)
                            .build();
                    minioClient.makeBucket(makeBucketArgs);
                } catch (Exception e) {
                    throw new MinioException("Cannot create bucket", e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return minioClient;
    }

}
