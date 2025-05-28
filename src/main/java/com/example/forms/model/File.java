package com.example.forms.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class File {

    private Integer id;
    private String fileName;
    private String filePath;
    private String contentType;
    private LocalDateTime uploadDate;
    private String createdBy;
    private String description;

    public File(String fileName, String filePath, String contentType, 
        LocalDateTime uploadDate, String createdBy, String description) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.uploadDate = uploadDate;
        this.createdBy = createdBy;
        this.description = description;
    }

    public File() {}
}