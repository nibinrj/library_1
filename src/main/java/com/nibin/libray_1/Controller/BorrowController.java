package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
import com.nibin.libray_1.Service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
public class BorrowController {


    @Autowired
    private BorrowService service;

    @PostMapping("/borrow")
    public void  borrow(@RequestBody BorrowRequest borrow){
        service.borrow_book(borrow);
    }


}
