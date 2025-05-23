package com.nibin.libray_1.Repository;

import com.nibin.libray_1.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface user_repo extends JpaRepository<Users,Integer> {
}
