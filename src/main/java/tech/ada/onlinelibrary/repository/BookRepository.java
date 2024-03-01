package tech.ada.onlinelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.domain.enums.Genre;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(Genre genre);
}
