package com.nibin.libray_1.model;

import java.time.LocalDate;

public record LoanResponse(int id, int bookId, int userId, LocalDate borrowedDate, LocalDate returnedDate) {

    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getUser().getId(),
                loan.getBorrowedDate(),
                loan.getReturnedDate());
    }
}
