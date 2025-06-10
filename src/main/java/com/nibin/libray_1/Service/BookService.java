package com.nibin.libray_1.Service;


import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Repository.Book_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private Book_Repo repo;


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

    public boolean delete_book_by_id(int id)
    {
        if(!repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
            return false;

    }
}
