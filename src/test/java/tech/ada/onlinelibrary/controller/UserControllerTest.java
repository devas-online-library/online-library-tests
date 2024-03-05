package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.repository.UserRepository;
import tech.ada.onlinelibrary.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    private User user;

    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;



    @BeforeEach
    public void setup() {
        user = new User(1L, "userTest","123", "test@gmail.com" );
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void login() {
    }

    @Test
    public void createUserHttpTest() throws Exception {

        when (modelMapper.map(Mockito.any(),User.class)).thenReturn(user);
        //Arrange - Preparar
        when(userService.createUser(Mockito.any())).thenReturn(user);

        //Act - Açao
        mockMvc.perform(MockMvcRequestBuilders.post("/library/user/register").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(user))).andExpect(status().isCreated());

        //Assertion - Validacao
        verify(userService, times(1)).createUser(Mockito.any());
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUserLoans() {
    }
}
