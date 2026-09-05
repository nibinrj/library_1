package com.nibin.libray_1.service;

import com.nibin.libray_1.exception.ConflictException;
import com.nibin.libray_1.exception.NotFoundException;
import com.nibin.libray_1.model.Book;
import com.nibin.libray_1.repository.BookRepository;
import com.nibin.libray_1.repository.LoanRepository;
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
    private BookRepository repository;
    @Autowired
    private LoanRepository loanRepository;

    @CachePut(value = "book", key = "#result.id")
    public Book create(Book book) {
        return repository.save(book);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "book", key = "#id")
    public Book findById(int id) {
        log.info("Finding book with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
    }

    @CacheEvict(value = "book", key = "#id")
    public boolean deleteById(int id) {
        if (!repository.existsById(id)) {
            return false;
        }
        // Loans are kept as history, so they still reference the book.
        if (loanRepository.existsByBookId(id)) {
            throw new ConflictException("Book " + id + " has loan history and cannot be deleted");
        }
        repository.deleteById(id);
        return true;
    }
}
