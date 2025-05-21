package com.example.forms.model;


import lombok.Data;


@Data
public class User {
    private Integer id;
    private String username;
    private String email;
    private Integer idRole;

    public User(Integer id, String username, String email, Integer idRole) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.idRole = idRole;
        
    }

    @Override
    public String toString() {
        return "User{" +
                " id='" + getId() + "'" +
                ", username='" + getUsername() + "'" +
                ", email='" + getEmail() + "'" +
                ", idRole='" + getIdRole() + "'" +
                "}";
    }
}
