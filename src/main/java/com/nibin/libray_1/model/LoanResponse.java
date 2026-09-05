package com.nibin.libray_1.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "LoanResponse", description = "A borrowing record. Kept after the book comes back, with returnedDate filled in.")
public record LoanResponse(

        @Schema(description = "Pass this to the return endpoint", example = "1")
        int id,

        @Schema(example = "1")
        int bookId,

        @Schema(example = "1")
        int userId,

        @Schema(example = "2026-09-05")
        LocalDate borrowedDate,

        @Schema(description = "Null while the book is still out on loan", example = "null", nullable = true)
        LocalDate returnedDate) {

    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getUser().getId(),
                loan.getBorrowedDate(),
                loan.getReturnedDate());
    }
}
