package com.nibin.libray_1.model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @Positive(message = "must be a positive id")
    private int userId;

    @Positive(message = "must be a positive id")
    private int bookId;
}
