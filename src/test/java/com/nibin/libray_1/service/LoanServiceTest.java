package com.nibin.libray_1.service;

import com.nibin.libray_1.exception.ConflictException;
import com.nibin.libray_1.exception.NotFoundException;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanServiceTest {

    @Autowired
    private LoanService loanService;
    @Autowired
    private BookService bookService;
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

        Book book = new Book();
        book.setName("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setCopies(1);
        bookId = bookRepository.save(book).getId();

        User user = new User();
        user.setUsername("nibin");
        userId = userRepository.save(user).getId();
    }

    @Test
    void borrowingDecrementsCopiesAndCreatesALoan() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));

        assertEquals(0, bookRepository.findById(bookId).orElseThrow().getCopies());
        assertEquals(LocalDate.now(), loan.getBorrowedDate());
        assertFalse(loan.isReturned());
    }

    @Test
    void borrowingTheSameBookTwiceIsRejected() {
        loanService.borrow(new LoanRequest(userId, bookId));

        assertThrows(ConflictException.class,
                () -> loanService.borrow(new LoanRequest(userId, bookId)));
    }

    @Test
    void borrowingWithNoCopiesLeftIsRejected() {
        User other = new User();
        other.setUsername("someone else");
        int otherId = userRepository.save(other).getId();
        loanService.borrow(new LoanRequest(userId, bookId));

        assertThrows(ConflictException.class,
                () -> loanService.borrow(new LoanRequest(otherId, bookId)));
    }

    @Test
    void returningRestoresTheCopyAndAllowsReborrowing() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));

        Loan returned = loanService.returnLoan(loan.getId());

        assertEquals(1, bookRepository.findById(bookId).orElseThrow().getCopies());
        assertEquals(LocalDate.now(), returned.getReturnedDate());
        assertDoesNotThrow(() -> loanService.borrow(new LoanRequest(userId, bookId)));
    }

    @Test
    void returningTwiceIsRejectedAndDoesNotInflateCopies() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));
        loanService.returnLoan(loan.getId());

        assertThrows(ConflictException.class, () -> loanService.returnLoan(loan.getId()));
        assertEquals(1, bookRepository.findById(bookId).orElseThrow().getCopies());
    }

    @Test
    void theLoanIsKeptAsHistoryAfterReturning() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));

        loanService.returnLoan(loan.getId());

        assertTrue(loanRepository.findById(loan.getId()).orElseThrow().isReturned());
    }

    @Test
    void borrowingAnUnknownBookIsNotFound() {
        assertThrows(NotFoundException.class,
                () -> loanService.borrow(new LoanRequest(userId, 9999)));
    }

    @Test
    void returningAnUnknownLoanIsNotFound() {
        assertThrows(NotFoundException.class, () -> loanService.returnLoan(9999));
    }

    @Test
    void aBookWithLoanHistoryCannotBeDeleted() {
        Loan loan = loanService.borrow(new LoanRequest(userId, bookId));
        loanService.returnLoan(loan.getId());

        assertThrows(ConflictException.class, () -> bookService.deleteById(bookId));
    }
}
