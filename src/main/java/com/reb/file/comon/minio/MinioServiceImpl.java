package com.reb.file.comon.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioServiceImpl implements MinioService {

    @Value("${minio.bucket}")
    private String bucket;

    @Autowired
    private MinioClient minioClient;

    @Override
    public void uploadFile(Path path, InputStream file, String contentType) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(path.toString())
                .stream(file, file.available(), -1)
                .contentType(contentType)
                .build();
        minioClient.putObject(args);
    }

    @Override
    public StatObjectResponse getMetadata(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucket)
                .object(path.toString())
                .build();
        return minioClient.statObject(args);
    }
}
