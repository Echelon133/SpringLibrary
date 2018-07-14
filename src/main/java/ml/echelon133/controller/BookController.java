package ml.echelon133.controller;

import ml.echelon133.exception.FailedFieldValidationException;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Author;
import ml.echelon133.model.Book;
import ml.echelon133.model.Genre;
import ml.echelon133.model.dto.BookDto;
import ml.echelon133.service.IAuthorService;
import ml.echelon133.service.IBookService;
import ml.echelon133.service.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class BookController {

    private IBookService bookService;
    private IAuthorService authorService;
    private IGenreService genreService;

    @Autowired
    public BookController(IBookService bookService, IAuthorService authorService, IGenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @RequestMapping(value = "api/books", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> getBooks(@RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "author", required = false) String author,
                                               @RequestParam(value = "genre", required = false) String genre) {
        List<Book> books;
        if (title != null) {
            books = bookService.findAllByTitleContaining(title);
        } else if (author != null) {
            books = bookService.findAllByAuthorsContainingName(author);
        } else if (genre != null) {
            books = bookService.findAllByGenresContainingName(genre);
        } else {
            books = bookService.findAll();
        }
        return new ResponseEntity<>(books , HttpStatus.OK);
    }

    @RequestMapping(value = "api/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable Long id) throws ResourceNotFoundException {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "api/books", method = RequestMethod.POST)
    public ResponseEntity<Book> postBook(@Valid @RequestBody BookDto bookDto, BindingResult result)
            throws FailedFieldValidationException, ResourceNotFoundException {
        if (result.hasErrors()) {
            throw new FailedFieldValidationException(result.getFieldErrors());
        }
        Set<Author> bookAuthors = new HashSet<>();
        List<Genre> bookGenres = new ArrayList<>();

        for (Long authorId : bookDto.getAuthorIds()) {
            bookAuthors.add(authorService.findById(authorId));
        }

        for (Long genreId : bookDto.getGenreIds()) {
            bookGenres.add(genreService.findById(genreId));
        }

        Book book = new Book(bookDto.getTitle(), bookAuthors, bookGenres);
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

}
