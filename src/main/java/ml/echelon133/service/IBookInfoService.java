package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.BookInfo;

public interface IBookInfoService {
    BookInfo findById(Long id) throws ResourceNotFoundException;
    BookInfo save(BookInfo bookInfo);
}
