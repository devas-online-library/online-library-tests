package tech.ada.onlinelibrary.advice.exception;

public class UserNotFoundException extends NullPointerException {

    public UserNotFoundException(Long id){ super("Could not find user with id: " + id); }
}
