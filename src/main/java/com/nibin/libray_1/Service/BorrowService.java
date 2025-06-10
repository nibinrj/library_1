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

@Service
public class BorrowService {

    @Autowired
    private Book_Repo book_repo;

    @Autowired
    private User_Repo user_repo;

    @Autowired
    private Borrow_Repo borrow_repo;

    public void borrow_book(BorrowRequest borrowRequest)
    {
        Book book = book_repo.findById(borrowRequest.getBookId()).orElseThrow(()-> new RuntimeException(("No Book Id found")));
        Users user = user_repo.findById(borrowRequest.getUserId()).orElseThrow(() -> new RuntimeException("No User Id Found"));

        if(book.getCopies()==0)
        {
            return;
        }
        book.setCopies(book.getCopies()-1);
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUsers(user);
        borrow.setBorrowed_date(LocalDate.now());

        borrow_repo.save(borrow);
    }

    public void returnRequest(BorrowRequest borrow )
    {

    }
}
