package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Author;
import ml.echelon133.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService implements IAuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> findAllByNameContaining(String name) {
        return authorRepository.findAllByNameContaining(name);
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author findById(Long id) throws ResourceNotFoundException {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return author.get();
        } else {
            throw new ResourceNotFoundException("Author with this id not found");
        }
    }

    @Override
    public boolean deleteById(Long id) throws ResourceNotFoundException {
        boolean exists = authorRepository.existsById(id);
        if (exists) {
            authorRepository.deleteById(id);
            exists = authorRepository.existsById(id);
        } else {
            throw new ResourceNotFoundException("Author with this id not found");
        }
        return !exists;
    }
}
