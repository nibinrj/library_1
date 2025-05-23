package com.nibin.libray_1.Controller;


import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/book")
public class BookController {

    private BookService service;

    @PostMapping("/add")
    public Book add_book(@RequestBody Book book )
    {
        return service.add_book(book);
    }

    @GetMapping("/get_all")
    public List<Book> get_book()
    {
        return service.get_books();
    }

    @GetMapping("/get/{id}")
    public Book find_by_id(@PathVariable int id )
    {
        return service.find_by_id(id);
    }

}
