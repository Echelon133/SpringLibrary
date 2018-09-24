package ml.echelon133.book;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Size;
import java.util.List;

public class PatchBookDto {

    @Length(min=1, max=255)
    private String title;

    @Size(min = 1, max = 10)
    private List<Long> authorIds;

    @Size(min = 1, max = 10)
    private List<Long> genreIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }
}