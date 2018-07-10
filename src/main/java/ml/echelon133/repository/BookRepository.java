package ml.echelon133.repository;

import ml.echelon133.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join b.genres as genre where genre.name like :genreName")
    List<Book> findAllByGenresNameLike(@Param("genreName") String genreName);

    @Query("select b from Book b join b.authors as author where author.name like :authorName")
    List<Book> findAllByAuthorsNameLike(@Param("authorName") String authorName);

    List<Book> findAllByTitleContaining(String title);
}
