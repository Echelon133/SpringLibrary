package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Entry;

import java.util.List;

public interface IEntryService {

    enum AddedSince {
        DAY, WEEK, MONTH, YEAR
    }

    Entry save(Entry entry);
    List<Entry> findAllByBookTitleContaining(String title);
    List<Entry> findAllByUsername(String username);
    List<Entry> findAllByAddedSince(AddedSince when);
    List<Entry> findAllByReturned(Boolean returned);
    List<Entry> findAll();
    Entry findById(Long id) throws ResourceNotFoundException;
}
