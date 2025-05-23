package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/user")
public class UserController {


    @Autowired
    private UserService service;

    @PostMapping("/add")
    public ResponseEntity<Users> createUser(@RequestBody Users user)
    {
        ResponseEntity<Users> res;
        res = new ResponseEntity<>(service.create_user(user), HttpStatus.OK);
        return res;
    }


    public ResponseEntity<List<Users>> get_users()
    {
        List<Users> users = service.get_users();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
