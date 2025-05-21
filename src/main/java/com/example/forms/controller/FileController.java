package com.example.forms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.forms.service.MinioService;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final MinioService minioService;

    public FileController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String objectName = file.getOriginalFilename();
            minioService.uploadFile(objectName, file);
            return ResponseEntity.ok("File uploaded successfully: " + objectName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<String> getDownloadUrl(@RequestParam String objectName) {
    try {
        if (!minioService.fileExists(objectName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
        
        String downloadUrl = minioService.getPresignedDownloadUrl(objectName, 3600);
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