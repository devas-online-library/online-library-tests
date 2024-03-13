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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    public void testUserRegister() {
        User user = new User(1L, "testUser", "password123", "test@example.com");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.userRegister(user);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1L, "testUser", "password123", "test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.updateUser(user);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User(1L, "testUser", "password123", "test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals(user, result);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAuthenticateUser() {
        String username = "testUser";
        String password = "password123";
        User user = new User(1L, username, password, "test@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, password);

        assertTrue(result);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testMustFailInAuthenticateUser() {
        String username = "testUser";
        String password = "password123";
        User user = new User(1L, username, "wrongPassword", "test@example.com");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticateUser(username, password);

        assertTrue(result);

        verify(userRepository, times(1)).findByUsername(username);
    }


    @Test
    public void testAuthenticateUserNotFound() {
        String username = "nonExistingUser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.authenticateUser(username, password);

        assertFalse(result);

        verify(userRepository, times(1)).findByUsername(username);
    }
}