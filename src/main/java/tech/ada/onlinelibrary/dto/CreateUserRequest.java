package tech.ada.onlinelibrary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    private String userPassword;
    private String email;

    public CreateUserRequest(Long userId, String username, String userPassword, String email) {
        this.username = Objects.requireNonNull(username);
        this.userPassword = Objects.requireNonNull(userPassword);
        this.email = Objects.requireNonNull(email);
    }
}
