package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Repository.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private User_Repo repo;

    public Users create_user(Users user){
        return repo.save(user);
    }

    public List<Users> get_users() {
        return repo.findAll();
    }
}
