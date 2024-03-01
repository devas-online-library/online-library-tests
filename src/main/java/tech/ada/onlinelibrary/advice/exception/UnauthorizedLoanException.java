package tech.ada.onlinelibrary.advice.exception;

public class UnauthorizedLoanException extends RuntimeException {

    public UnauthorizedLoanException(Long userId){ super("User with id: " + userId + " is not authorized to make new loans."); }
}
