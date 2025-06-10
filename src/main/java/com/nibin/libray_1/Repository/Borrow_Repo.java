package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Borrow_Repo extends JpaRepository< Borrow,Integer> {
}
