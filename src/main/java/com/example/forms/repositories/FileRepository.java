package com.example.forms.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.example.forms.model.File;

@Repository
public class FileRepository {

    private final JdbcClient jdbcClient;

    public FileRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<File> findAll() {
        return jdbcClient.sql("SELECT * FROM files").query(File.class).list();
    }

    public Optional<File> findByFileName(String fileName) {
        return jdbcClient.sql("SELECT * FROM files WHERE file_name = :fileName")
                .param("fileName", fileName)
                .query(File.class)
                .optional();
    }

    public void create(File file) {
        var updated = jdbcClient.sql("INSERT INTO files (file_name, file_path, content_type, size, upload_date) VALUES (?,?,?,?,?)")
                .params(List.of(
                        file.getFileName(),
                        file.getFilePath(),
                        file.getContentType(),
                        file.getSize(),
                        file.getUploadDate()))
                .update();
        Assert.state(updated == 1, "Failed to create file: " + file.getFileName());
    }

    public void deleteByFileName(String fileName) {
        var updated = jdbcClient.sql("DELETE FROM files WHERE file_name = :fileName")
                .param("fileName", fileName)
                .update();
        Assert.state(updated >= 0, "Failed to delete file: " + fileName);
    }
}