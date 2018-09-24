package ml.echelon133.entry;

import ml.echelon133.exception.FailedFieldValidationException;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.book.Book;
import ml.echelon133.register.User;
import ml.echelon133.book.IBookService;
import ml.echelon133.register.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "api/entries", method = RequestMethod.POST)
    public ResponseEntity<Entry> postEntry(@Valid @RequestBody NewEntryDto newEntryDto, BindingResult result)
            throws ResourceNotFoundException, FailedFieldValidationException {
        if (result.hasErrors()) {
            throw new FailedFieldValidationException(result.getFieldErrors());
        }
        Book book = bookService.findById(newEntryDto.getBorrowedBookId());
        User user = userService.findUserByUsername(newEntryDto.getBorrowerUsername());
        Entry entry = new Entry(book, user);

        Entry savedEntry = entryService.save(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @RequestMapping(value = "api/entries/{id}", method = RequestMethod.GET)
    public ResponseEntity<Entry> getEntry(@PathVariable Long id) throws ResourceNotFoundException {
        Entry entry = entryService.findById(id);
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @RequestMapping(value = "api/entries/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Entry> patchEntry(@PathVariable Long id,
                                            @Valid @RequestBody PatchEntryDto patchEntryDto,
                                            BindingResult result) throws ResourceNotFoundException,
                                                                         FailedFieldValidationException,
                                                                         BookAlreadyReturnedException {
        if (result.hasErrors()) {
            throw new FailedFieldValidationException(result.getFieldErrors());
        }

        Entry entry = entryService.findById(id);

        if (entry.getReturned()) {
            throw new BookAlreadyReturnedException("Book from this entry was already returned");
        }

        if (patchEntryDto.getReturned()) {
            // book was not returned yet
            entry.returnBook();
        }

        Entry savedEntry = entryService.save(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.OK);
    }
}
