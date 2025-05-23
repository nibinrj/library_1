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
}
