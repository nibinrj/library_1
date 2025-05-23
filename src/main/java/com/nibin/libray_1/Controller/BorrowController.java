package com.nibin.libray_1.Controller;

import com.nibin.libray_1.Model.Borrow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    public ResponseEntity<Void> borrow(@RequestBody Borrow)
}
