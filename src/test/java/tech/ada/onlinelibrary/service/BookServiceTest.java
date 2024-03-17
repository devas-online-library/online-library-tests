package tech.ada.onlinelibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.advice.exception.BookNotFoundException;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateBookRequest;
import tech.ada.onlinelibrary.repository.BookRepository;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private ModelMapper modelMapper;

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
    void getBooksByTittleTest() throws Exception {
        //Prepare
        String title = "Clean Code: A Handbook of Agile Software Craftsmanship";

        // Act
        bookService.getBooksByTitle(title);

        //Assertion
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(title);

    }

    @Test
    void getBooksByAuthorTest() throws Exception {
        //Prepare
        String author = "Robert C. Martin";

        // Act
        bookService.getBooksByAuthor(author);

        //Assertion
        verify(bookRepository, times(1)).findByAuthor(author);

    }

    @Test
    void getBooksByGenreTest() throws Exception {
        //Prepare
        Genre genre = Genre.TECHNICAL;

        // Act
        bookService.getBooksByGenre(genre);

        //Assertion
        verify(bookRepository, times(1)).findByGenre(genre);

    }

    @Test
    void createBookTest() {
        // Prepare
        CreateBookRequest bookRequest = new CreateBookRequest("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));

        when(modelMapper.map(Mockito.any(CreateBookRequest.class), Mockito.any())).thenReturn(book);

        // Act
        Book createdBook = bookService.createBook(bookRequest);

        // Assertion
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getBookByIdTest() throws Exception {
        //Prepare
        Long id = 1l;

        //Act and Assertion
        assertThrows(BookNotFoundException.class, ()->bookService.getBookById(id));
    }

    @Test
    void deleteBookTest() throws Exception {
        //Prepare
        Long id = 1l;
        when(bookService.getBookById(id)).thenReturn(book);

        // Act
        bookService.deleteBook(id);

        //Assertion
        verify(bookRepository, times(1)).deleteById(id);

    }


}
