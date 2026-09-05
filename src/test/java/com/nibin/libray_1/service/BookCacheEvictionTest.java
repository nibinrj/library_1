package com.nibin.libray_1.service;

import com.nibin.libray_1.model.Book;
import com.nibin.libray_1.model.Loan;
import com.nibin.libray_1.model.LoanRequest;
import com.nibin.libray_1.model.User;
import com.nibin.libray_1.repository.BookRepository;
import com.nibin.libray_1.repository.LoanRepository;
import com.nibin.libray_1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The rest of the suite runs with caching disabled, so stale-cache bugs slip through.
 * This one uses a real (in-memory) cache to check that writes invalidate reads.
 */
@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=simple")
class BookCacheEvictionTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanRepository loanRepository;

    private int bookId;
    private int userId;

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        cacheManager.getCache("book").clear();

        Book book = new Book();
        book.setName("Dune");
        book.setAuthor("Herbert");
        book.setCopies(1);
        bookId = bookService.create(book).getId();

        User user = new User();
        user.setUsername("nibin");
        userId = userRepository.save(user).getId();
    }

    @Test
    void borrowingInvalidatesTheCachedBook() {
        assertEquals(1, bookService.findById(bookId).getCopies());

        loanService.borrow(new LoanRequest(userId, bookId));

        assertEquals(0, bookService.findById(bookId).getCopies());
    }

    @Test
    void returningInvalidatesTheCachedBook() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));
        assertEquals(0, bookService.findById(bookId).getCopies());

        loanService.returnLoan(loan.getId());

        assertEquals(1, bookService.findById(bookId).getCopies());
    }

    @Test
    void deletingEvictsTheCachedBook() {
        bookService.findById(bookId);

        bookService.deleteById(bookId);

        assertNull(cacheManager.getCache("book").get(bookId));
    }
}
