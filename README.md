# Library Management System

A simple RESTful API for a library management system built with Java and Spring Boot. This application allows for managing books, users, and borrowing records.

## Features

* **Book Management**: Perform CRUD (Create, Read, Delete) operations for books in the library.
* **User Management**: Add and retrieve library users.
* **Borrowing System**: Allows users to borrow available books, which decrements the book's copy count.

## Technologies Used

* **Java 24**
* **Spring Boot 3.4.5**
* **Spring Data JPA**: For data persistence and database operations.
* **PostgreSQL**: As the relational database.
* **Maven**: For project build and dependency management.

## API Endpoints

The following are the available API endpoints for interacting with the application. The application runs on port `6969`.

### Book Controller

Base Path: `/book`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/add` | Adds a new book. | `{"name": "The Hobbit", "author": "J.R.R. Tolkien", "copies": 5}` |
| `GET` | `/get_all` | Retrieves a list of all books. | (None) |
| `GET` | `/get/{id}` | Finds a book by its ID. | (None) |
| `DELETE`| `/{id}` | Deletes a book by its ID. | (None) |

### User Controller

Base Path: `/users`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/add` | Adds a new user. | `{"username": "John Doe"}` |
| `GET` | `/getall` | Retrieves all users. | (None) |

### Borrow Controller

Base Path: `/borrow`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/borrow` | Borrows a book for a user. | `{"userId": 1, "bookId": 1}` |
