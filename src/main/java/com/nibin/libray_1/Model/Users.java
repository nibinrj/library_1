package com.nibin.libray_1.Model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_Id;

    @Column(name = "name")
    private String username;

    @Override
    public String toString() {
        return "Users{" +
                "user_Id=" + user_Id +
                ", username='" + username + '\'' +
                '}';
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users(int user_Id, String username) {
        this.user_Id = user_Id;
        this.username = username;
    }

    public Users() {
    }
}
