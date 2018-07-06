package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Genre;

import java.util.List;

public interface IGenreService {
    List<Genre> findAll();
    Genre save(Genre genre);
    Genre findById(Long id) throws ResourceNotFoundException;
    boolean deleteById(Long id) throws ResourceNotFoundException;
}
