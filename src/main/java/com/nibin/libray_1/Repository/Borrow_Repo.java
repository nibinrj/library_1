package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Borrow_Repo extends JpaRepository<Borrow, Integer> {
    Optional<Borrow> findByUsersIdAndBookId(int userId, int bookId);
}
