package ml.echelon133.controller;

import ml.echelon133.model.Entry;
import ml.echelon133.service.IBookService;
import ml.echelon133.service.IEntryService;
import ml.echelon133.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntryController {

    private IEntryService entryService;
    private IBookService bookService;
    private IUserService userService;

    @Autowired
    public EntryController(IEntryService entryService, IBookService bookService, IUserService userService) {
        this.entryService = entryService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @RequestMapping(value = "api/entries", method = RequestMethod.GET)
    public ResponseEntity<List<Entry>> getEntries(@RequestParam(value = "returned", required = false) Boolean returned,
                                                  @RequestParam(value = "bookTitle", required = false) String bookTitle,
                                                  @RequestParam(value = "username", required = false) String username,
                                                  @RequestParam(value = "since", required = false) String since) {
        List<Entry> entries;

        if (returned != null) {
            entries = entryService.findAllByReturned(returned);
        } else if (bookTitle != null) {
            entries = entryService.findAllByBookTitleContaining(bookTitle);
        } else if (username != null) {
            entries = entryService.findAllByUsername(username);
        } else if (since != null) {
            if (since.equalsIgnoreCase("day")) {
                entries = entryService.findAllByAddedSince(IEntryService.AddedSince.DAY);
            } else if (since.equalsIgnoreCase("week")) {
                entries = entryService.findAllByAddedSince(IEntryService.AddedSince.WEEK);
            } else if (since.equalsIgnoreCase("month")) {
                entries = entryService.findAllByAddedSince(IEntryService.AddedSince.MONTH);
            } else if (since.equalsIgnoreCase("year")) {
                entries = entryService.findAllByAddedSince(IEntryService.AddedSince.YEAR);
            } else {
                entries = entryService.findAll();
            }
        } else {
            entries = entryService.findAll();
        }
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }
}
