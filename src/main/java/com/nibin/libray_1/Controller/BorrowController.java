package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
import com.nibin.libray_1.Service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService service;
    @PostMapping("/borrow")
    public ResponseEntity<String>  borrow(@RequestBody BorrowRequest borrow) throws Exception {
        try{
        String s = service.borrow_book(borrow);
        return ResponseEntity.ok(s);}
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.ok(service.returnBook(request));
    }
}
