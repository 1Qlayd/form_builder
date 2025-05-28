package com.example.forms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.forms.service.MinioService;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/upload")
    public ResponseEntity<String> generateUploadUrl(
            @RequestParam String objectName,
            @RequestParam(defaultValue = "3600") int expiryInSeconds) {
        try {
            // Проверяем, не существует ли уже файл с таким именем
            if (minioService.fileExists(objectName)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("File with this name already exists");
            }
            
            String uploadUrl = minioService.getPresignedUploadUrl(objectName, expiryInSeconds);
            return ResponseEntity.ok(uploadUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate upload URL: " + e.getMessage());
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