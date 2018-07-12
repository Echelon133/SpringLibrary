package ml.echelon133.controller;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Book;
import ml.echelon133.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
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
}
