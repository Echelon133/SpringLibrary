package ml.echelon133.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByUserBorrowing_Username(String username);
    List<Entry> findAllByDateStartedAfter(Date date);
    List<Entry> findAllByReturned(Boolean returned);

    @Query("select e from Entry e join e.bookBorrowed as b where b.title like :bookTitle")
    List<Entry> findAllByBookTitleLike(@Param("bookTitle") String bookTitle);



}
