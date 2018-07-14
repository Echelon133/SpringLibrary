package ml.echelon133.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class BookDto {

    @NotNull
    @Length(min=1, max=255)
    private String title;

    @NotNull
    @Size(min = 1, max = 10)
    private List<Long> authorIds;

    @NotNull
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
