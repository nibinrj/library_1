package com.nibin.libray_1.Controller;


import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping("/add")
    public ResponseEntity<Book> add_book(@RequestBody Book book )
    {
        ResponseEntity<Book> entity  = new ResponseEntity<>(service.add_book(book),HttpStatus.OK);
        return entity;
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<Book>> get_book()
    {
        ResponseEntity<List<Book>> entity  = new ResponseEntity<>(service.get_books(),HttpStatus.OK);
        return entity;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> find_by_id(@PathVariable int id )
    {
        ResponseEntity<Book> entity = new ResponseEntity<>(service.find_by_id(id),HttpStatus.OK);
        return entity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete_by_id(int id)
    {
        if(service.delete_book_by_id(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
