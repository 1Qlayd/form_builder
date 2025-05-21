package com.example.forms.model;

import lombok.Data;

@Data
public class Form {
    private Integer id;
    private String name;
    private Integer userID;
    private String description;
    private String content;
    private String date;

    public Form(Integer id, String name, Integer userID, String description, String content, String date) {
        this.id = id;
        this.name = name;
        this.userID = userID;   
        this.description = description;
        this.content = content;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Form{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + (userID != null ? userID.toString() : "null") +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}