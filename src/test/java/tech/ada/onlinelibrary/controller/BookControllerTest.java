package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateBookRequest;
import tech.ada.onlinelibrary.repository.BookRepository;
import tech.ada.onlinelibrary.service.BookService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;

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
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
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
        //Arrange - Preparar
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].id", equalTo(1)));


    }


    @Test
    void getBooksByTitleTest() throws Exception {
        //Arrange - Preparar
        String title = "Clean Code: A Handbook of Agile Software Craftsmanship";
        when(bookService.getBooksByTitle(title)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("title", title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", equalTo("Clean Code: A Handbook of Agile Software Craftsmanship")));


    }

    @Test
    void getBooksByAuthorTest() throws Exception {
        //Arrange - Preparar
        String author = "Robert C. Martin";
        when(bookService.getBooksByAuthor(author)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("author", author)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].author", equalTo("Robert C. Martin")));

    }

    @Test
    void getBooksByGenreTest() throws Exception {
        //Arrange - Preparar
        Genre genre = Genre.TECHNICAL;
        when(bookService.getBooksByGenre(genre)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books")
                        .param("genre", String.valueOf(genre))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].genre", equalTo(String.valueOf(Genre.TECHNICAL))));

    }

    @Test
    void createBookTest() throws Exception {
        //Arrange - Preparar
        CreateBookRequest bookRequest = new CreateBookRequest("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));

        when(bookService.createBook(Mockito.any(CreateBookRequest.class))).thenReturn(book);

        //Act
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
    void deleteBookTest() throws Exception {
        //Arrange - Preparar
        Long id = 1l;

//        when(bookService.getBookById(id)).thenReturn(book);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.delete("/library/loans/{id}", id))
                .andExpect(status().isNoContent());

    }
}

