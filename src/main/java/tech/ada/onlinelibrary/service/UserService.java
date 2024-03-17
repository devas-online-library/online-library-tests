package tech.ada.onlinelibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public Optional<User> userRegister (User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            return Optional.empty();

        return Optional.of(userRepository.save(user));
    }


    public Optional<User> updateUser(User user) {
        user.setUserPassword(user.getUserPassword());
        return Optional.of(userRepository.save(user));
    }

    public User createUser(User user){
        User newUser = userRepository.save(user);
        return newUser;
    }

    public boolean authenticateUser(String username, String password) {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String storedPassword = user.getUserPassword();
                return storedPassword.equals(password);
            } else {
                return false;
            }
        }
}
