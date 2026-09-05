package com.nibin.libray_1.controller;

import com.nibin.libray_1.model.LoanRequest;
import com.nibin.libray_1.model.LoanResponse;
import com.nibin.libray_1.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    /** Borrowing a book creates a loan. */
    @PostMapping
    public ResponseEntity<LoanResponse> borrow(@Valid @RequestBody LoanRequest request) {
        return new ResponseEntity<>(LoanResponse.from(service.borrow(request)), HttpStatus.CREATED);
    }

    /** Returning is a state transition on an existing loan, not a new resource. */
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnLoan(@PathVariable int id) {
        return ResponseEntity.ok(LoanResponse.from(service.returnLoan(id)));
    }
}
