package com.example.forms.service;

import com.example.forms.model.File;
import com.example.forms.repositories.FileRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    
    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioService(MinioClient minioClient, FileRepository fileRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
    }

    public void uploadFile(String objectName, MultipartFile file, String createdBy, String description) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        
        saveFileMetadata(objectName, file.getContentType(), createdBy, description);
    }

    public void saveFileMetadata(String objectName, String contentType, String createdBy, String description) {
        try {
            log.debug("Saving metadata for file: {}", objectName);
            File fileEntity = new File(
                objectName,
                bucketName + "/" + objectName,
                contentType,
                LocalDateTime.now(),
                createdBy,
                description
            );
            
            fileRepository.create(fileEntity);
            log.info("Successfully saved metadata for file: {}", objectName);
        } catch (Exception e) {
            log.error("Failed to save metadata for file {}: {}", objectName, e.getMessage(), e);
            throw new RuntimeException("Failed to save file metadata", e);
        }
    }

    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        
        fileRepository.deleteByFileName(objectName);
    }
    
public boolean fileExists(String objectName) {
        try {
            log.debug("Checking if file exists: {}", objectName);
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.debug("File {} exists in MinIO", objectName);
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                log.debug("File {} does not exist in MinIO", objectName);
                return false;
            }
            log.error("Error checking file existence for {}: {}", objectName, e.getMessage(), e);
            throw new RuntimeException("Error checking file existence", e);
        } catch (Exception e) {
            log.error("Unexpected error checking file existence for {}: {}", objectName, e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    public String getPresignedDownloadUrl(String objectName, int expiryInSeconds) throws Exception {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expiryInSeconds)
                .build()
        );
    }

    public String getPresignedUploadUrl(String objectName, int expiryInSeconds) {
        try {
            log.debug("Generating presigned URL for {} (expiry: {}s)", objectName, expiryInSeconds);
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expiryInSeconds)
                    .build()
            );
            log.info("Successfully generated presigned URL for {}", objectName);
            return url;
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for {}: {}", objectName, e.getMessage(), e);
            throw new RuntimeException("Failed to generate upload URL", e);
        }
    }
}