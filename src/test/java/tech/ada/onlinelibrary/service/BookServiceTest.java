package tech.ada.onlinelibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.repository.BookRepository;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private Book book;
    private List<Book> books = new ArrayList<>();
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookService).build();

        book = new Book(1l, "Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));
        books.add(book);
    }
    @Test
    void getAllBooksTest() throws Exception {
        //Act
        List<Book> result = bookService.getAllBooks();

        //Assertion
        verify(bookRepository, times(1)).findAll();

    }

    @Test
    void getAllBooksByTittleTest() throws Exception {
        //Prepare
        String title = "Clean Code: A Handbook of Agile Software Craftsmanship";

        // Act
        bookService.getBooksByTitle(title);

        //Assertion
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(title);

    }

    @Test
    void getAllBooksByAuthorTest() throws Exception {
        //Prepare
        String author = "Robert C. Martin";

        // Act
        bookService.getBooksByAuthor(author);

        //Assertion
        verify(bookRepository, times(1)).findByAuthor(author);

    }

    @Test
    void getAllBooksByGenreTest() throws Exception {
        //Prepare
        Genre genre = Genre.TECHNICAL;

        // Act
        bookService.getBooksByGenre(genre);

        //Assertion
        verify(bookRepository, times(1)).findByGenre(genre);

    }

}
