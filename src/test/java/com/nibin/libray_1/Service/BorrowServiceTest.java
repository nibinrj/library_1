package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
import com.nibin.libray_1.exception.ConflictException;
import com.nibin.libray_1.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BorrowServiceTest {

    @Autowired
    private BorrowService borrowService;
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

        Book book = new Book();
        book.setName("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setCopies(1);
        bookId = book_repo.save(book).getId();

        Users user = new Users();
        user.setUsername("nibin");
        userId = user_repo.save(user).getId();
    }

    @Test
    void borrowDecrementsCopies() {
        borrowService.borrow_book(new BorrowRequest(userId, bookId));

        assertEquals(0, book_repo.findById(bookId).orElseThrow().getCopies());
    }

    @Test
    void borrowingLastCopyTwiceIsRejected() {
        borrowService.borrow_book(new BorrowRequest(userId, bookId));

        assertThrows(ConflictException.class,
                () -> borrowService.borrow_book(new BorrowRequest(userId, bookId)));
    }

    @Test
    void returnRestoresCopyAndAllowsReborrow() {
        BorrowRequest request = new BorrowRequest(userId, bookId);
        borrowService.borrow_book(request);

        borrowService.returnBook(request);

        assertEquals(1, book_repo.findById(bookId).orElseThrow().getCopies());
        assertTrue(borrow_repo.findByUsersIdAndBookId(userId, bookId).isEmpty());
        assertDoesNotThrow(() -> borrowService.borrow_book(request));
    }

    @Test
    void returningTwiceDoesNotInflateCopies() {
        BorrowRequest request = new BorrowRequest(userId, bookId);
        borrowService.borrow_book(request);
        borrowService.returnBook(request);

        assertThrows(NotFoundException.class, () -> borrowService.returnBook(request));
        assertEquals(1, book_repo.findById(bookId).orElseThrow().getCopies());
    }

    @Test
    void borrowingUnknownBookIsNotFound() {
        assertThrows(NotFoundException.class,
                () -> borrowService.borrow_book(new BorrowRequest(userId, 9999)));
    }
}
