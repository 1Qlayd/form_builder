package com.example.forms.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.example.forms.model.User;

@Repository
public class UserRepository {
    private JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<User>findAllUsers(){
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public Optional<User> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .optional();
    }

    public void create(User user) {
        var updated =  jdbcClient.sql("INSERT INTO USERS (id, username, email, idRole) VALUES (?,?,?,?)")
                .params(List.of(user.getId(), user.getUsername(), user.getEmail(),user.getIdRole()))
                .update();
        Assert.state(updated == 1,"Ошибка в create" + user.getUsername());
    }

    public void update(User user, Integer id) {
        var updated = jdbcClient.sql("UPDATE USERS SET username = ?, email = ?, idRole = ? WHERE id = ?")
                .params(List.of(user.getUsername(), user.getEmail(),user.getIdRole(), id))
                .update();
        Assert.state(updated == 1,"Ошибка в update" + user.getUsername());
    }

    public void delete(Integer id) {
        var updated =  jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
                .param("id", id)
                .update();
                Assert.state(updated == 1,"Ошибка в delete" + id);
    }

    public int count(){
        return jdbcClient.sql("SElECT * FROM USERS").query().listOfRows().size();
    }
    
    public void saveAll(List<User> user){
        user.stream().forEach(this::create);
    }

}
