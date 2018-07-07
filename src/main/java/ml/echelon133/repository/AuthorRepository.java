package ml.echelon133.repository;

import ml.echelon133.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByNameContaining(String name);
}
