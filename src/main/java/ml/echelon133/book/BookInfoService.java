package ml.echelon133.book;

import ml.echelon133.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookInfoService implements IBookInfoService {

    private BookInfoRepository bookInfoRepository;

    @Autowired
    public BookInfoService(BookInfoRepository bookInfoRepository) {
        this.bookInfoRepository = bookInfoRepository;
    }

    @Override
    public BookInfo findById(Long id) throws ResourceNotFoundException {
        Optional<BookInfo> bookInfo = bookInfoRepository.findById(id);
        if (bookInfo.isPresent()) {
            return bookInfo.get();
        } else {
            throw new ResourceNotFoundException("BookInfo with this id not found");
        }
    }

    @Override
    public BookInfo save(BookInfo bookInfo) {
        return bookInfoRepository.save(bookInfo);
    }
}
