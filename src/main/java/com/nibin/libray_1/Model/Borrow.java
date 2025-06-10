package com.nibin.libray_1.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrow")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrow_id;

    @ManyToOne
    @JoinColumn(name = "user_Id ",referencedColumnName = "Id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "book_Id",referencedColumnName = "Id")
    private Book book;

    private LocalDate Borrowed_date;


    public int getBorrow_id() {
        return borrow_id;
    }

    public void setBorrow_id(int borrow_id) {
        this.borrow_id = borrow_id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowed_date() {
        return Borrowed_date;
    }

    public void setBorrowed_date(LocalDate borrowed_date) {
        Borrowed_date = borrowed_date;
    }

    public Borrow(int borrow_id, Users users, Book book, LocalDate borrowed_date) {
        this.borrow_id = borrow_id;
        this.users = users;
        this.book = book;
        Borrowed_date = borrowed_date;
    }

    public Borrow() {
    }
}
