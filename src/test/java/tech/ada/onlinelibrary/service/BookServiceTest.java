package tech.ada.onlinelibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBooksByTittleTest() throws Exception {
        // Arrange
        String title = "Clean Code: A Handbook of Agile Software Craftsmanship";

        // Act
        bookService.getBooksByTitle(title);

        // Assert
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(title);

    }

    @Test
    void getBooksByAuthorTest() throws Exception {
        // Arrange
        String author = "Robert C. Martin";

        // Act
        bookService.getBooksByAuthor(author);

        // Assert
        verify(bookRepository, times(1)).findByAuthor(author);

    }

    @Test
    void getBooksByGenreTest() throws Exception {
        // Arrange
        Genre genre = Genre.TECHNICAL;

        // Act
        bookService.getBooksByGenre(genre);

        // Assert
        verify(bookRepository, times(1)).findByGenre(genre);
    }

    @Test
    void createBookTest() {
        // Arrange
        CreateBookRequest bookRequest = new CreateBookRequest("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));

        when(modelMapper.map(Mockito.any(CreateBookRequest.class), Mockito.any())).thenReturn(book);

        // Act
        Book createdBook = bookService.createBook(bookRequest);

        // Assert
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getBookByIdTest() throws Exception {
        //Prepare
        Long id = 1l;

        //Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(id));
    }

    @Test
    void deleteBookTest() throws Exception {
        // Arrange
        Long id = 1l;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        bookService.deleteBook(id);

        // Assert
        verify(bookRepository, times(1)).deleteById(id);

    }

}

