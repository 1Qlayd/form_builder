package com.example.forms.model;

import lombok.Data;

@Data
public class File {

    private Integer id;
    private String fileName;
    private String filePath;
    private String contentType;
    private Long size;
    private String uploadDate;

    public File(String fileName, String filePath, String contentType, Long size, String uploadDate) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
        this.uploadDate = uploadDate;
    }

    public File() {}
}