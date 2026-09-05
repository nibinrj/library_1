package com.nibin.libray_1.controller;

import com.nibin.libray_1.exception.ErrorResponse;
import com.nibin.libray_1.model.LoanRequest;
import com.nibin.libray_1.model.LoanResponse;
import com.nibin.libray_1.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@Tag(name = "Loans", description = "Borrowing and returning books")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping
    @Operation(summary = "Borrow a book",
            description = "Creates a loan and decrements the copy count. Keep the returned loan id: "
                    + "it is what the return endpoint takes.")
    @ApiResponse(responseCode = "201", description = "Loan created")
    @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No such book or user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "No copies left, or the user already has this book",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LoanResponse> borrow(@Valid @RequestBody LoanRequest request) {
        return new ResponseEntity<>(LoanResponse.from(service.borrow(request)), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return a borrowed book",
            description = "A state transition on an existing loan, not a new resource. The loan is kept "
                    + "as history with its returned date rather than deleted.")
    @ApiResponse(responseCode = "200", description = "Book returned")
    @ApiResponse(responseCode = "404", description = "No loan with that id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "The loan was already returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LoanResponse> returnLoan(
            @Parameter(description = "Id from the borrow response", example = "1") @PathVariable int id) {
        return ResponseEntity.ok(LoanResponse.from(service.returnLoan(id)));
    }
}
