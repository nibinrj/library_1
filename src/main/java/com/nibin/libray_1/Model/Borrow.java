package com.nibin.libray_1.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrow")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrow_id;

    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "Id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "book_Id", referencedColumnName = "Id")
    private Book book;

    private LocalDate Borrowed_date;
}
