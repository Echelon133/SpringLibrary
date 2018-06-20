package ml.echelon133.controller;

import ml.echelon133.model.Genre;
import ml.echelon133.service.IGenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

    private IGenreService genreService;

    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @RequestMapping(value = "api/genres", method = RequestMethod.GET)
    public ResponseEntity<List<Genre>> getGenres() {
        List<Genre> genres = genreService.findAll();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }
}
