package com.nibin.libray_1.Service;

import com.nibin.libray_1.Model.Book;
import com.nibin.libray_1.Model.Borrow;
import com.nibin.libray_1.Model.BorrowRequest;
import com.nibin.libray_1.Model.Users;
import com.nibin.libray_1.Repository.Book_Repo;
import com.nibin.libray_1.Repository.Borrow_Repo;
import com.nibin.libray_1.Repository.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

@Service
public class BorrowService {

    @Autowired
    private Book_Repo book_repo;
    @Autowired
    private User_Repo user_repo;
    @Autowired
    private Borrow_Repo borrow_repo;
    public String borrow_book(BorrowRequest borrowRequest) throws Exception {
        Book book = book_repo.findById(borrowRequest.getBookId()).orElseThrow(()-> new RuntimeException(("No Book Id found")));
        Users user = user_repo.findById(borrowRequest.getUserId()).orElseThrow(() -> new RuntimeException("No User Id Found"));
        Optional<Borrow> res = borrow_repo.findByUsersIdAndBookId(borrowRequest.getUserId(), borrowRequest.getBookId());
        if(res.isPresent()){
            throw new Exception("The user has already borrowed this book");
        }
        if(book.getCopies()==0)
        {
            throw new Exception("No Copies left");
        }
        book.setCopies(book.getCopies()-1);
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUsers(user);
        borrow.setBorrowed_date(LocalDate.now());

        borrow_repo.save(borrow);
        return "The Book has been successfully borrowed";
    }
    public String returnBook(BorrowRequest request) {
        Borrow borrow = borrow_repo
                .findByUsersIdAndBookId(request.getUserId(), request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not borrowed or already returned"));
        Book book = borrow.getBook();
        book.setCopies(book.getCopies() + 1);
        book_repo.save(book);

        borrow_repo.save(borrow);
        return "Book returned successfully";
    }
}
