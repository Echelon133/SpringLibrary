package ml.echelon133.entry;

import ml.echelon133.exception.ResourceNotFoundException;

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
