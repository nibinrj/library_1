package com.nibin.libray_1.Model;

import java.time.LocalDate;

public class BorrowRequest {
    private int userId;
    private int bookId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public BorrowRequest(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public BorrowRequest() {
    }
}
