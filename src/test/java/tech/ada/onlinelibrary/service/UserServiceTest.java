package tech.ada.onlinelibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserRegisterSuccessForNewUser() {
        User newUser = new User(1L, "newUser", "password", "newUser@example.com");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Optional<User> result = userService.userRegister(newUser);

        assertTrue(result.isPresent());
        assertEquals(newUser, result.get());

    }
    @Test
    void testUserRegisterFailsForDuplicatedUser() {

        User existingUser = new User(1L, "existingUser", "password", "existingUser@example.com");

        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.userRegister(existingUser);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1L, "testUser", "password123", "test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.updateUser(user);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

    }

    @Test
    public void testCreateUser() {
        User user = new User(1L, "testUser", "password123", "test@example.com");

        when(userRepository.save(user)).thenReturn(user);
        User result = userService.createUser(user);

        assertEquals(user, result);

    }

    @Test
    public void testAuthenticateUser() {
        String username = "testUser";
        String password = "password123";
        User user = new User(1L, username, password, "test@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, password);

        assertTrue(result);

    }

    @Test
    public void testAuthenticateUserIncorrectPassword() {
        String username = "testUser";
        String password = "password123";
        String wrongPassword = "wrongPassword";
        User user = new User(1L, username, password, "test@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, wrongPassword);

        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        String username = "nonExistingUser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.authenticateUser(username, password);

        assertFalse(result);
    }
}
