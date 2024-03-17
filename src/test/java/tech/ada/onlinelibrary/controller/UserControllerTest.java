
package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.dto.CreateUserRequest;
import tech.ada.onlinelibrary.repository.UserRepository;
import tech.ada.onlinelibrary.service.UserService;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private User user;
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        user = new User("testUser", "123", "test@gmail.com");
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {

        User userLogin = new User();
        userLogin.setUsername("testUser");
        userLogin.setUserPassword("password123");


        when(userService.authenticateUser("testUser", "password123")).thenReturn(true);


        mockMvc.perform(MockMvcRequestBuilders.post("/library/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userLogin)))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        User updatedUser = new User(1L, "updatedUser", "updatedPassword", "updated@example.com");

        User userRequest = new User(1L, "testUser", "123", "test@example.com");

        when(userService.updateUser(any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(MockMvcRequestBuilders.put("/library/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isOk());

    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testUser");
        userRequest.setUserPassword("password123");
        userRequest.setEmail("test@example.com");


        User user = new User(userRequest.getUsername(), userRequest.getUserPassword(), userRequest.getEmail());

        when(modelMapper.map(any(), eq(User.class))).thenReturn(user);


        when(userService.createUser(any(User.class))).thenReturn(user);


        mockMvc.perform(MockMvcRequestBuilders.post("/library/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect( jsonPath("$.userPassword").value(user.getUserPassword()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

    }



    @Test
    public void testDeleteUser_Success() throws Exception {
        Long userId = 1L;

        User userToDelete = new User(userId, "testUser", "123", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        mockMvc.perform(MockMvcRequestBuilders.delete("/library/user/{id}", userId))
                .andExpect(status().isNoContent()); // Expect NO_CONTENT status as user is deleted

    }

    @Test
    public void testGetAllUserLoans_Success() throws Exception {
        Long userId = 1L;

        List<Loan> userLoans = Arrays.asList(
                new Loan(1L, new Book(1L, "Book 1", "Author 1", Genre.FICTION, "Publisher 1", Year.of(2020)), null, LocalDate.now(), LocalDate.now(), null)
        );

        User user = new User(userId, "testUser", "123", "test@example.com");
        user.setLoans(userLoans);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/library/user/{id}/loans", userId))
                .andExpect(status().isOk()) // Expect OK status
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((ResultMatcher) jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].book.id", is(1)))
                .andExpect(jsonPath("$[0].book.title", is("Book 1")))
                .andExpect(jsonPath("$[0].book.author", is("Author 1")))
                .andExpect(jsonPath("$[0].book.genre", is("FICTION")))
                .andExpect(jsonPath("$[0].book.publisher", is("Publisher 1")))
                .andExpect(jsonPath("$[0].book.publicationYear", is(2020)));

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

