package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book_Repo extends JpaRepository<Book,Integer> {
}
