package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.advice.RestControllerAdvice;
import tech.ada.onlinelibrary.advice.exception.BookNotFoundException;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateBookRequest;
import tech.ada.onlinelibrary.repository.BookRepository;
import tech.ada.onlinelibrary.service.BookService;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private Book book;
    private List<Book> books = new ArrayList<>();

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setup() {
        book = new Book(1l, "Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));
        books.add(book);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).setControllerAdvice(RestControllerAdvice.class).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

@Test
    void getAllBooksTest() throws Exception {
        //Arrange
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].id", equalTo(1)));


    }


    @Test
    void getBooksByTitleTest() throws Exception {
        //Arrange
        String title = "Clean Code: A Handbook of Agile Software Craftsmanship";
        when(bookService.getBooksByTitle(title)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("title", title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", equalTo("Clean Code: A Handbook of Agile Software Craftsmanship")));

    }

    @Test
    void getBooksByAuthorTest() throws Exception {
        // Arrange
        String author = "Robert C. Martin";
        when(bookService.getBooksByAuthor(author)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("author", author)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].author", equalTo("Robert C. Martin")));

    }

    @Test
    void getBooksByGenreTest() throws Exception {
        // Arrange
        Genre genre = Genre.TECHNICAL;
        when(bookService.getBooksByGenre(genre)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("genre", String.valueOf(genre))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].genre", equalTo(String.valueOf(Genre.TECHNICAL))));

    }

    @Test
    void createBookTest() throws Exception {
        //Arrange
        CreateBookRequest bookRequest = new CreateBookRequest("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));

        when(bookService.createBook(Mockito.any(CreateBookRequest.class))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/library/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code: A Handbook of Agile Software Craftsmanship"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));

    }

    @Test
    void deleteBookTest_ValidId_NoContentReturned() throws Exception {
        //Arrange
        Long id = 1l;
        doNothing().when(bookService).deleteBook(id);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/library/books/{id}", id))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteBookTest_InvalidId_BookNotFoundExceptionThrown() throws Exception {
        //Arrange
        Long id = 1l;
        doThrow(new BookNotFoundException(id)).when(bookService).deleteBook(id);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/library/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Could not find book with id: 1"));

    }
}

