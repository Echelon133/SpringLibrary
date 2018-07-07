package ml.echelon133.controller;

import ml.echelon133.model.Author;
import ml.echelon133.service.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {

    private IAuthorService authorService;

    @Autowired
    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(value = "api/authors", method = RequestMethod.GET)
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(value = "name", required = false) String name) {
        List<Author> authors;
        if (name == null) {
            authors = authorService.findAll();
        } else {
            authors = authorService.findAllByNameContaining(name);
        }
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
}
