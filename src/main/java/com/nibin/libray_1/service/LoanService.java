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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class LoanService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Transactional
    @CacheEvict(value = "book", key = "#request.bookId")
    public Loan borrow(LoanRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("No book found with id: " + request.getBookId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("No user found with id: " + request.getUserId()));

        loanRepository.findByUserIdAndBookIdAndReturnedDateIsNull(request.getUserId(), request.getBookId())
                .ifPresent(existing -> {
                    throw new ConflictException("The user has already borrowed this book");
                });
        if (book.getCopies() == 0) {
            throw new ConflictException("No copies left");
        }

        book.setCopies(book.getCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setBorrowedDate(LocalDate.now());
        return loanRepository.save(loan);
    }

    @Transactional
    @CacheEvict(value = "book", key = "#result.book.id")
    public Loan returnLoan(int loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found: " + loanId));
        if (loan.isReturned()) {
            throw new ConflictException("Loan " + loanId + " was already returned on " + loan.getReturnedDate());
        }

        Book book = loan.getBook();
        book.setCopies(book.getCopies() + 1);
        bookRepository.save(book);

        loan.setReturnedDate(LocalDate.now());
        return loanRepository.save(loan);
    }
}
