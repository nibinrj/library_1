package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Repository.Book_Repo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {
    @Autowired
    private Book_Repo repo;

    @CachePut(value = "book", key = "#result.id")
    public Book add_book(Book book)
    {
        return repo.save(book);
    }


    public List<Book> get_books() {
        List<Book> books=repo.findAll();
        return books;
    }

    @Transactional
    @Cacheable(value = "book", key = "#id")
    public Book find_by_id(int id) {
        log.info("Finding book with id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public boolean delete_book_by_id(int id) {
        if(repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
            return false;
    }

}
