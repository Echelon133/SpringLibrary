package ml.echelon133.service;

import ml.echelon133.model.Genre;

import java.util.List;

public interface IGenreService {
    List<Genre> findAll();
    Genre save(Genre genre);
}
