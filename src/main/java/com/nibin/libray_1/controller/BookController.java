package com.nibin.libray_1.controller;

import com.nibin.libray_1.exception.ErrorResponse;
import com.nibin.libray_1.model.Book;
import com.nibin.libray_1.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "The library catalogue")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping
    @Operation(summary = "Add a book",
            description = "The id is assigned by the server; any id sent in the body is ignored.")
    @ApiResponse(responseCode = "201", description = "Book created")
    @ApiResponse(responseCode = "400", description = "Invalid book",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        return new ResponseEntity<>(service.create(book), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all books")
    @ApiResponse(responseCode = "200", description = "Every book in the catalogue")
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one book",
            description = "Served from the Redis cache when available, and from the database otherwise.")
    @ApiResponse(responseCode = "200", description = "The book")
    @ApiResponse(responseCode = "404", description = "No book with that id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Book> findById(
            @Parameter(description = "Id of the book", example = "1") @PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book",
            description = "Rejected if the book has ever been borrowed, because loans are kept as history.")
    @ApiResponse(responseCode = "204", description = "Book deleted")
    @ApiResponse(responseCode = "404", description = "No book with that id")
    @ApiResponse(responseCode = "409", description = "The book has loan history",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Id of the book", example = "1") @PathVariable int id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
