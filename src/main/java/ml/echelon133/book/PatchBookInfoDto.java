package ml.echelon133.book;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

public class PatchBookInfoDto {

    @Digits(integer = 4, fraction = 0)
    @Positive
    private Integer numberOfPages;

    @Length(min = 3, max = 30)
    private String language;

    @Digits(integer = 4, fraction = 0)
    @Positive
    private Integer publicationYear;

    @Length(min = 10, max = 500)
    private String description;

    @ISBN
    private String isbn;

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
