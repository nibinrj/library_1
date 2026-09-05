package com.nibin.libray_1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoanRequest", description = "Which user is borrowing which book")
public class LoanRequest {

    @Positive(message = "must be a positive id")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int userId;

    @Positive(message = "must be a positive id")
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int bookId;
}
