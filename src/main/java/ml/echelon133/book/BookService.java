package ml.echelon133.book;

import ml.echelon133.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean deleteById(Long id) throws ResourceNotFoundException {
        boolean exists = bookRepository.existsById(id);
        if (exists) {
            bookRepository.deleteById(id);
            exists = bookRepository.existsById(id);
        } else {
            throw new ResourceNotFoundException("Book with this id not found");
        }
        return !exists;
    }

    @Override
    public Book findById(Long id) throws ResourceNotFoundException {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new ResourceNotFoundException("Book with this id not found");
        }
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAllByGenresContainingName(String genreName) {
        // Wrap genreName in '%' before it is passed to the JPQL query
        String genreLike = "%" + genreName + "%";
        return bookRepository.findAllByGenresNameLike(genreLike);
    }

    @Override
    public List<Book> findAllByAuthorsContainingName(String authorName) {
        // Wrap authorName in '%' before it is passed to the JPQL query
        String authorLike = "%" + authorName + "%";
        return bookRepository.findAllByAuthorsNameLike(authorLike);
    }

    @Override
    public List<Book> findAllByTitleContaining(String title) {
        // This method does not need any wrapping of the argument, because
        // underneath it uses regular JpaRepository generated query, and not JPQL custom query
        return bookRepository.findAllByTitleContaining(title);
    }
}
