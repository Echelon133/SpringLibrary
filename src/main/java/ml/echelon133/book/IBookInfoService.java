package ml.echelon133.book;

import ml.echelon133.exception.ResourceNotFoundException;

public interface IBookInfoService {
    BookInfo findById(Long id) throws ResourceNotFoundException;
    BookInfo save(BookInfo bookInfo);
}
