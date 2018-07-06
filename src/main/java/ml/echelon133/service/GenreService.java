package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Genre;
import ml.echelon133.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService implements IGenreService {

    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre findById(Long id) throws ResourceNotFoundException {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return genre.get();
        } else {
            throw new ResourceNotFoundException("Genre with this id not found");
        }
    }

    @Override
    public boolean deleteById(Long id) throws ResourceNotFoundException {
        boolean exists = genreRepository.existsById(id);
        if (exists) {
            genreRepository.deleteById(id);
            exists = genreRepository.existsById(id);
        } else {
            throw new ResourceNotFoundException("Genre with this id not found");
        }
        return !exists;
    }
}
