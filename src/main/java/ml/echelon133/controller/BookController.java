package ml.echelon133.controller;

import ml.echelon133.model.Book;
import ml.echelon133.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        if (genre != null) {
            books = bookService.findAllByGenresContainingName(genre);
        } else if (title != null) {
            books = bookService.findAllByTitleContaining(title);
        } else {
            books = bookService.findAll();
        }
        return new ResponseEntity<>(books , HttpStatus.OK);
    }
}
