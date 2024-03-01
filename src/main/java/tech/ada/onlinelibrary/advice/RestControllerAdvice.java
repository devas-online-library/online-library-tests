package tech.ada.onlinelibrary.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.ada.onlinelibrary.advice.exception.BookNotFoundException;
import tech.ada.onlinelibrary.advice.exception.UnauthorizedLoanException;
import tech.ada.onlinelibrary.advice.exception.UserNotFoundException;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedLoanException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedLoanException(UnauthorizedLoanException exception){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

}

