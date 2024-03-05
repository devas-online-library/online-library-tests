/*
package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.service.BookService;

import java.time.Year;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;
    private Book book;
    private List<Book> books;

    @InjectMocks
    private BookController bookController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        book = new Book(1l, "Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", Genre.TECHNICAL, "Prentice Hall", Year.of(2008));
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

        PrintStream MockMvcResultHandlers;
        mockMvc.perform(MockMvcRequestBuilders.get("/library/books").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(book))).
                andDo(MockMvcResultHandlers.print());

        verify(bookService,times(1)).getAllBooks();
    }


    @Test
    void getBooksByTitle() {
    }

    @Test
    void getBooksByAuthor() {
    }

    @Test
    void getBooksByGenre() {
    }

    @Test
    void createBook() throws Exception {
        //Arrange - Preparar
        when(bookService.createBook(Mockito.any())).thenReturn(book);

        //Act - Açao
        mockMvc.perform(MockMvcRequestBuilders.post("/library/books").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(book))).andExpect(status().isCreated());

        //Assertion - Validacao
        verify(bookService, times(1)).createBook(Mockito.any());
    }

    @Test
    void deleteBook() {
    }
}
*/
