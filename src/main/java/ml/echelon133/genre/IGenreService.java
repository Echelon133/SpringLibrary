package ml.echelon133.genre;

import ml.echelon133.exception.ResourceNotFoundException;

import java.util.List;

public interface IGenreService {
    List<Genre> findAll();
    Genre save(Genre genre);
    Genre findById(Long id) throws ResourceNotFoundException;
    boolean deleteById(Long id) throws ResourceNotFoundException;
}
