package com.example.forms.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.forms.model.User;
import com.example.forms.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('client_admin')")
    List<User> findAll(){
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    Optional<User> findById(@PathVariable Integer id ) {
        return userService.findById(id);
    }
      
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody User user){
        userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody User user, @PathVariable Integer id){
        userService.updateUser(user, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id){
        userService.deleteUser(id);
    }

}
