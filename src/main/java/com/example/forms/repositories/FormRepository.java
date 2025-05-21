package com.example.forms.repositories;


import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.example.forms.model.Form;

@Repository
public class FormRepository {

    private JdbcClient jdbcClient;

    public FormRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Form>findAllForms(){
        return jdbcClient.sql("SElECT * FROM FORM").query(Form.class).list();
    }

    public Optional<Form> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM FORM WHERE id = :id")
                .param("id", id)
                .query(Form.class)
                .optional();
    }

    public void create(Form form) {
        var updated =  jdbcClient.sql("INSERT INTO FORM (id, name, userID, description, content, date) VALUES (?,?,?,?,?,?)")
                .params(List.of(form.getId(), form.getName(), form.getUserID(), form.getDescription(),form.getContent(), form.getDate().toString()))
                .update();
        Assert.state(updated == 1,"Ошибка в create" + form.getName());
    }

    public void update(Form form, Integer id) {
        var updated = jdbcClient.sql("UPDATE FORM SET name = ?, userID = ?, description = ?, content = ?, date = ? WHERE id = ?")
                .params(List.of(form.getName(), form.getUserID(), form.getDescription(),form.getContent(), form.getDate().toString(), id))
                .update();
        Assert.state(updated == 1,"Ошибка в update" + form.getName());
    }

    public void delete(Integer id) {
        var updated =  jdbcClient.sql("DELETE FROM FORM WHERE id = :id")
                .param("id", id)
                .update();
                Assert.state(updated == 1,"Ошибка в delete" + id);
    }

    public int count(){
        return jdbcClient.sql("SELECT * FROM FORM").query().listOfRows().size();
    }
    
    public void saveAll(List<Form> form){
        form.stream().forEach(this::create);
    }

}