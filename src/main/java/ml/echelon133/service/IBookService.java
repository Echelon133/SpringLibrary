package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Book;

import java.util.List;

public interface IBookService {
    boolean deleteById(Long id) throws ResourceNotFoundException;
    Book findById(Long id) throws ResourceNotFoundException;
    Book save(Book book);
    List<Book> findAll();
    List<Book> findAllByGenresContainingName(String genreName);
    List<Book> findAllByAuthorsContainingName(String authorName);
    List<Book> findAllByTitleContaining(String title);
}
