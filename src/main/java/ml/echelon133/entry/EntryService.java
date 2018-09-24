package ml.echelon133.entry;

import ml.echelon133.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EntryService implements IEntryService {

    private EntryRepository entryRepository;

    @Autowired
    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public Entry save(Entry entry) {
        return entryRepository.save(entry);
    }

    @Override
    public List<Entry> findAllByBookTitleContaining(String title) {
        String likeTitle = "%" + title + "%";
        return entryRepository.findAllByBookTitleLike(likeTitle);
    }

    @Override
    public List<Entry> findAllByUsername(String username) {
        return entryRepository.findAllByUserBorrowing_Username(username);
    }

    @Override
    public List<Entry> findAllByAddedSince(AddedSince when) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        switch (when) {
            case DAY:
                calendar.add(Calendar.HOUR, -24);
                date = calendar.getTime();
                break;
            case WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                date = calendar.getTime();
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
                date = calendar.getTime();
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
                date = calendar.getTime();
                break;
        }
        return entryRepository.findAllByDateStartedAfter(date);
    }

    @Override
    public List<Entry> findAllByReturned(Boolean returned) {
        return entryRepository.findAllByReturned(returned);
    }

    @Override
    public List<Entry> findAll() {
        return entryRepository.findAll();
    }

    @Override
    public Entry findById(Long id) throws ResourceNotFoundException {
        Optional<Entry> entry = entryRepository.findById(id);
        if (entry.isPresent()) {
            return entry.get();
        } else {
            throw new ResourceNotFoundException("Entry with this id not found");
        }
    }
}
