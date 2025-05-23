package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Service.UserService;
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
    public ResponseEntity<Users> createUser(@RequestBody Users user)
    {
        ResponseEntity<Users> res;
        System.out.println(user.toString());
        Users u = service.create_user(user);
        System.out.println(u.toString());
        res = new ResponseEntity<>(u, HttpStatus.OK);
        return res;
    }


    @GetMapping("/getall")
    public ResponseEntity<List<Users>> get_users()
    {
        List<Users> users = service.get_users();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
