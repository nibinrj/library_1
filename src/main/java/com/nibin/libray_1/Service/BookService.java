package com.nibin.libray_1.Service;


import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Repository.book_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private book_repo repo;

    public Book add_book(Book book)
    {
        return repo.save(book);
    }

    public List<Book> get_books()
    {
        List<Book> books=repo.findAll();
        return books;
    }

    public Book find_by_id(int id)
    {
        Book book = repo.getById(id);
        return book;
    }
}
