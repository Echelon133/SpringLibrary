package ml.echelon133.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
public class BookInfo {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name="gen", strategy = "foreign", parameters = @Parameter(name="property", value="bookInfoOwner"))
    private Long id;
    private Integer numberOfPages;
    private String language;
    private Integer publicationYear;
    private String description;
    private String isbn;

    @OneToOne
    @JsonIgnore
    private Book bookInfoOwner;

    public BookInfo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Book getBookInfoOwner() {
        return bookInfoOwner;
    }

    public void setBookInfoOwner(Book bookInfoOwner) {
        this.bookInfoOwner = bookInfoOwner;
    }
}
