package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface borrow_repo extends JpaRepository< Borrow,Integer> {
}
