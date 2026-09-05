package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Model.Borrow;
import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
import com.nibin.libray_1.exception.ConflictException;
import com.nibin.libray_1.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowService {

    @Autowired
    private Book_Repo book_repo;
    @Autowired
    private User_Repo user_repo;
    @Autowired
    private Borrow_Repo borrow_repo;
    @Transactional
    @CacheEvict(value = "book", key = "#borrowRequest.bookId")
    public String borrow_book(BorrowRequest borrowRequest) {
        Book book = book_repo.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new NotFoundException("No book found with id: " + borrowRequest.getBookId()));
        Users user = user_repo.findById(borrowRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("No user found with id: " + borrowRequest.getUserId()));
        Optional<Borrow> res = borrow_repo.findByUsersIdAndBookId(borrowRequest.getUserId(), borrowRequest.getBookId());
        if(res.isPresent()){
            throw new ConflictException("The user has already borrowed this book");
        }
        if(book.getCopies()==0)
        {
            throw new ConflictException("No copies left");
        }
        book.setCopies(book.getCopies()-1);
        book_repo.save(book);

        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUsers(user);
        borrow.setBorrowed_date(LocalDate.now());

        borrow_repo.save(borrow);
        return "The Book has been successfully borrowed";
    }

    @Transactional
    @CacheEvict(value = "book", key = "#request.bookId")
    public String returnBook(BorrowRequest request) {
        Borrow borrow = borrow_repo
                .findByUsersIdAndBookId(request.getUserId(), request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not borrowed or already returned"));
        Book book = borrow.getBook();
        book.setCopies(book.getCopies() + 1);
        book_repo.save(book);

        borrow_repo.delete(borrow);
        return "Book returned successfully";
    }
}
