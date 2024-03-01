package tech.ada.onlinelibrary.advice.exception;

public class BookNotFoundException extends NullPointerException {

    public  BookNotFoundException(Long id){ super("Could not find book with id: " + id); }
}
