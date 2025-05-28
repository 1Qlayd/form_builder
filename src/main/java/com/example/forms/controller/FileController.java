package com.example.forms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.forms.service.MinioService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/files")
public class FileController {

    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

@GetMapping("/upload")
public ResponseEntity<String> generateUploadUrl(
        @RequestParam String objectName,
        @RequestParam(defaultValue = "3600") int expiryInSeconds,
        @RequestParam(required = false) String createdBy,
        @RequestParam(required = false) String description) {
    try {
        if (objectName == null || objectName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("File name cannot be empty");
        }

        if (minioService.fileExists(objectName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("File already exists");
        }
        String uploadUrl = minioService.getPresignedUploadUrl(objectName, expiryInSeconds);

        minioService.saveFileMetadata(objectName, createdBy, description, uploadUrl);

        return ResponseEntity.ok(uploadUrl);
    } catch (Exception e) {
        log.error("Upload failed", e);
        return ResponseEntity.internalServerError()
                .body("Error: " + e.getMessage());
    }
}

    @GetMapping("/download")
    public ResponseEntity<String> generateDownloadUrl(
            @RequestParam String objectName,
            @RequestParam(defaultValue = "3600") int expiryInSeconds) {
        try {
            if (!minioService.fileExists(objectName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
            
            String downloadUrl = minioService.getPresignedDownloadUrl(objectName, expiryInSeconds);
            return ResponseEntity.ok(downloadUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate download URL: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String objectName) {
        try {
            if (!minioService.fileExists(objectName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + objectName);
            }
            minioService.deleteFile(objectName);
            return ResponseEntity.ok("File deleted successfully: " + objectName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file: " + e.getMessage());
        }
    }
}