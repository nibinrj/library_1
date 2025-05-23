package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface book_repo extends JpaRepository<Book,Integer> {
}
