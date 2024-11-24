package org.example.paperless_services.service;

import io.minio.*;
import org.example.paperless_services.config.StorageConfig;
import org.example.paperless_services.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
public class StorageService {
    private final MinioClient minioClient;
    private final String bucketName;

    @Autowired
    public StorageService(StorageConfig config) {

        this.minioClient = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
        this.bucketName = config.getBucketName();

        try {
            if (!minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new StorageException("Could not initialize MinIO bucket: " + bucketName, e);
        }
    }

    public void store(MultipartFile file, String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to store file in MinIO with name: " + fileName, e);
        }
    }

    public Resource load(String filename) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            throw new StorageException("Failed to load file from MinIO", e);
        }
    }

    public void delete(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to delete file from MinIO", e);
        }
    }
}




