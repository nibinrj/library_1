# Library Management System

A simple RESTful API for a library management system built with Java and Spring Boot. This application allows for managing books, users, and borrowing records.

## Features

* **Book Management**: Perform CRUD (Create, Read, Delete) operations for books in the library.
* **User Management**: Add and retrieve library users.
* **Borrowing System**: Users borrow available books (decrementing the copy count) and return them.

## Technologies Used

* **Java 21**
* **Spring Boot 3.4.5**
* **Spring Data JPA**: For data persistence and database operations.
* **H2**: In-memory relational database (data is not persisted across restarts).
* **Redis**: Caching layer for book lookups.
* **Maven**: For project build and dependency management.

## Running Locally

```bash
./mvnw spring-boot:run
```

The application runs on port `8099`. Redis is optional for local runs — set `REDIS_URL` to point at
an instance, otherwise it falls back to `redis://localhost:6379`. See `.env.example`.

Interactive API docs: <http://localhost:8099/swagger-ui.html>

### Docker

```bash
./mvnw package
docker build -t library-app .
docker run -p 8099:8099 -e REDIS_URL=<your redis url> library-app
```

Redis is optional: if it is unreachable the app logs a warning and serves every request
straight from the database.

## API Endpoints

### Book Controller

Base Path: `/book`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/add` | Adds a new book. Returns 201. | `{"name": "The Hobbit", "author": "J.R.R. Tolkien", "copies": 5}` |
| `GET` | `/all` | Retrieves a list of all books. | (None) |
| `GET` | `/{id}` | Finds a book by its ID. | (None) |
| `DELETE`| `/{id}` | Deletes a book by its ID. | (None) |

### User Controller

Base Path: `/users`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/add` | Adds a new user. Returns 201. | `{"username": "John Doe"}` |
| `GET` | `/getall` | Retrieves all users. | (None) |

### Borrow Controller

Base Path: `/borrow`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/borrow` | Borrows a book for a user. | `{"userId": 1, "bookId": 1}` |
| `POST` | `/return` | Returns a borrowed book. | `{"userId": 1, "bookId": 1}` |

### Error responses

Errors come back as JSON: `{"timestamp": "...", "status": 404, "message": "Book not found: 7"}`.
`404` for unknown ids, `409` for conflicts (already borrowed, no copies left), `400` for validation failures.
