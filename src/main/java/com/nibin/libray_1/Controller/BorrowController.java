package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService service;
    @PostMapping("/borrow")
    public ResponseEntity<String>  borrow(@Valid @RequestBody BorrowRequest borrow) {
        return ResponseEntity.ok(service.borrow_book(borrow));
    }
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(service.returnBook(request));
    }
}
