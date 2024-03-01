package tech.ada.onlinelibrary.dto;


import java.util.Objects;

public class CreateLoanRequest {

    private Long userId;
    private Long bookId;

    public CreateLoanRequest(Long userId, Long bookId) {
        this.userId = Objects.requireNonNull(userId);
        this.bookId = Objects.requireNonNull(bookId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }


}
