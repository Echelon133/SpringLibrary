package ml.echelon133.controller;

import ml.echelon133.model.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GenreController {

    @RequestMapping(value = "api/genres", method = RequestMethod.GET)
    public ResponseEntity<List<Genre>> getGenres() {
        List<Genre> genres = new ArrayList<>();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }
}
