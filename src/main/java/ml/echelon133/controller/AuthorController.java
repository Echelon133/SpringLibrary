package ml.echelon133.controller;

import ml.echelon133.exception.FailedFieldValidationException;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Author;
import ml.echelon133.model.dto.AuthorDto;
import ml.echelon133.service.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "api/authors/{id}", method = RequestMethod.GET)
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) throws ResourceNotFoundException {
        Author author = authorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @RequestMapping(value = "api/authors", method = RequestMethod.POST)
    public ResponseEntity<Author> postAuthor(@Valid @RequestBody AuthorDto authorDto, BindingResult result) throws FailedFieldValidationException {
        if (result.hasErrors()) {
            throw new FailedFieldValidationException(result.getFieldErrors());
        }
        Author author = new Author(authorDto.getName(), authorDto.getDescription());
        Author savedAuthor = authorService.save(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }

    @RequestMapping(value = "api/authors/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Author> putAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDto authorDto, BindingResult result)
        throws FailedFieldValidationException, ResourceNotFoundException {
        if (result.hasErrors()) {
            throw new FailedFieldValidationException(result.getFieldErrors());
        }
        Author author = authorService.findById(id);
        author.setName(authorDto.getName());
        author.setDescription(authorDto.getDescription());
        Author savedAuthor = authorService.save(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.OK);
    }

    @RequestMapping(value = "api/authors/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map> deleteAuthor(@PathVariable Long id) throws ResourceNotFoundException {
        Map<String, Boolean> response = new HashMap<>();
        boolean deleted = authorService.deleteById(id);
        if (deleted) {
            response.put("deleted", true);
        } else {
            response.put("deleted", false);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
