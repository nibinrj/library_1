package com.nibin.libray_1.Model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String name;
    private String author;
    private int copies ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public Book(int id, String name, String author, int copies) {
        Id = id;
        this.name = name;
        this.author = author;
        this.copies = copies;
    }

    public Book() {
    }
}
