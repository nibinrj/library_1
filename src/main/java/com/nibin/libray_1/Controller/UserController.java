package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/add")
    public ResponseEntity<Users> createUser(@Valid @RequestBody Users user) {
        return new ResponseEntity<>(service.create_user(user), HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Users>> get_users() {
        List<Users> users = service.get_users();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}


