package com.nibin.libray_1.repository;

import com.nibin.libray_1.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

    /** An active loan is one that has not been returned yet. */
    Optional<Loan> findByUserIdAndBookIdAndReturnedDateIsNull(int userId, int bookId);

    boolean existsByBookId(int bookId);
}
