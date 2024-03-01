package tech.ada.onlinelibrary.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.onlinelibrary.advice.exception.BookNotFoundException;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateBookRequest;
import tech.ada.onlinelibrary.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> getBooksByGenre(Genre genre) {
        return bookRepository.findByGenre(genre);
    }


    public Book createBook(CreateBookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public void deleteBook(Long id) {
        getBookById(id);
        bookRepository.deleteById(id);
    }

}
