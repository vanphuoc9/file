package com.reb.file.comon.minio;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

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
    public void uploadFile(String path, InputStream file, String contentType) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .stream(file, file.available(), -1)
                .contentType(contentType)
                .build();
        minioClient.putObject(args);
    }

    @Override
    public StatObjectResponse getMetadata(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build();
        return minioClient.statObject(args);
    }

    /**
     * Get metadata from minio
     * @param path
     * @param bucketFile
     * @return
     * @Info: Get metadata from bucketFile if bucketFile is not null, else default bucket
     */
    @Override
    public StatObjectResponse getMetadata(String path, String bucketFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketGet = bucket;
        if(Objects.nonNull(bucketFile)){
            bucketGet = bucketFile;
        }

        StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucketGet)
                .object(path)
                .build();
        return minioClient.statObject(args);
    }

    public InputStream get(Path path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(path.toString())
                .build();
        return minioClient.getObject(args);
    }

    public InputStream get(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build();
        return minioClient.getObject(args);
    }

    /**
     * Get file from minio
     * @param path
     * @param bucketFile
     * @return
     * @Info: Get file from bucketFile if bucketFile is not null, else default bucket
     */
    @Override
    public InputStream get(String path, String bucketFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketGet = bucket;
        if(Objects.nonNull(bucketFile)){
            bucketGet = bucketFile;
        }

        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketGet)
                .object(path)
                .build();
        return minioClient.getObject(args);
    }
}
