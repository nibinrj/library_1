package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
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
    private BorrowService borrowService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private Book_Repo book_repo;
    @Autowired
    private User_Repo user_repo;
    @Autowired
    private Borrow_Repo borrow_repo;

    private int bookId;
    private int userId;

    @BeforeEach
    void setUp() {
        borrow_repo.deleteAll();
        book_repo.deleteAll();
        user_repo.deleteAll();
        cacheManager.getCache("book").clear();

        Book book = new Book();
        book.setName("Dune");
        book.setAuthor("Herbert");
        book.setCopies(1);
        bookId = bookService.add_book(book).getId();

        Users user = new Users();
        user.setUsername("nibin");
        userId = user_repo.save(user).getId();
    }

    @Test
    void borrowingInvalidatesTheCachedBook() {
        assertEquals(1, bookService.find_by_id(bookId).getCopies());

        borrowService.borrow_book(new BorrowRequest(userId, bookId));

        assertEquals(0, bookService.find_by_id(bookId).getCopies());
    }

    @Test
    void returningInvalidatesTheCachedBook() {
        BorrowRequest request = new BorrowRequest(userId, bookId);
        borrowService.borrow_book(request);
        assertEquals(0, bookService.find_by_id(bookId).getCopies());

        borrowService.returnBook(request);

        assertEquals(1, bookService.find_by_id(bookId).getCopies());
    }

    @Test
    void deletingEvictsTheCachedBook() {
        bookService.find_by_id(bookId);

        bookService.delete_book_by_id(bookId);

        assertNull(cacheManager.getCache("book").get(bookId));
    }
}
