package com.nibin.libray_1.Model;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name")
    private String username;

    private boolean status;

    @Override
    public String toString() {
        return "Users{" +
                "user_Id=" + Id +
                ", username='" + username + '\'' +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users(int Id, String username) {
        this.Id = Id;
        this.username = username;
    }

    public Users() {
    }
}
