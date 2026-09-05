package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    @Cacheable(value = "book", key = "#id")
    public Book find_by_id(int id) {
        log.info("Finding book with id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
    }

    @CacheEvict(value = "book", key = "#id")
    public boolean delete_book_by_id(int id) {
        if(repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
            return false;
    }

}
