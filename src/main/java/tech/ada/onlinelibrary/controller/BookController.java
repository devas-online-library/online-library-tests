package tech.ada.onlinelibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateBookRequest;
import tech.ada.onlinelibrary.service.BookService;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/library/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/library/books", params = {"title"})
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title){
        List<Book> books = bookService.getBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/library/books", params = {"author"})
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/library/books", params = {"genre"})
    public ResponseEntity<List<Book>> getBooksByGenre(@RequestParam Genre genre) {
        List<Book> books = bookService.getBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/library/books")
    public ResponseEntity<Book> createBook(@RequestBody CreateBookRequest bookRequest){
        Book newbook = bookService.createBook(bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newbook);
    }

    @DeleteMapping("/library/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
