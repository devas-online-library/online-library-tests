package tech.ada.onlinelibrary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tech.ada.onlinelibrary.domain.enums.Genre;
import tech.ada.onlinelibrary.json.serializer.YearSerializer;

import java.time.Year;
import java.util.Objects;

public class CreateBookRequest {

    private String title;
    private String author;
    private Genre genre;
    private String publisher;

    @JsonSerialize(using = YearSerializer.class)
    private Year publicationYear;

    public CreateBookRequest(String title, String author, Genre genre, String publisher, Year publicationYear) {
        this.title = Objects.requireNonNull(title);
        this.author = Objects.requireNonNull(author);
        this.genre = Objects.requireNonNull(genre);
        this.publisher = Objects.requireNonNull(publisher);
        this.publicationYear = Objects.requireNonNull(publicationYear);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        this.publicationYear = publicationYear;
    }
}
