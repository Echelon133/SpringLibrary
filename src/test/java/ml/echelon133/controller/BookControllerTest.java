package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Author;
import ml.echelon133.model.Book;
import ml.echelon133.model.Genre;
import ml.echelon133.model.dto.NewBookDto;
import ml.echelon133.model.dto.PatchBookDto;
import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.service.IAuthorService;
import ml.echelon133.service.IBookService;
import ml.echelon133.service.IGenreService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private IBookService bookService;

    @Mock
    private IAuthorService authorService;

    @Mock
    private IGenreService genreService;

    @InjectMocks
    private BookController bookController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    private JacksonTester<List<Book>> jsonBooks;

    private JacksonTester<Book> jsonBook;

    private JacksonTester<NewBookDto> jsonBookDto;

    private JacksonTester<PatchBookDto> jsonPatchBookDto;

    private static List<Book> allBooks;
    private static List<Book> firstGenreBooks;
    private static List<Book> firstAuthorBooks;
    private static List<Book> firstTitleBooks;

    @BeforeClass
    public static void beforeSetup() {
        // Setup larger books list
        Author author1 = new Author("Test author1", "Test author1 description");
        Author author2 = new Author("Test author2", "Test author2 description");
        author1.setId(1L);
        author2.setId(2L);

        Genre genre1 = new Genre("Test genre1", "Test genre1 description");
        Genre genre2 = new Genre("Test genre2", "Test genre2 description");
        genre1.setId(1L);
        genre2.setId(2L);

        Set<Author> firstBookAuthors = new HashSet<>(Arrays.asList(author1));
        Set<Author> secondBookAuthors = new HashSet<>(Arrays.asList(author2));
        Set<Author> thirdBookAuthors = new HashSet<>(Arrays.asList(author1, author2));

        List<Genre> firstBookGenres = Arrays.asList(genre1);
        List<Genre> secondBookGenres = Arrays.asList(genre2);
        List<Genre> thirdBookGenres = Arrays.asList(genre1, genre2);

        Book book1 = new Book("First book", firstBookAuthors, firstBookGenres);
        book1.setId(1L);
        Book book2 = new Book("Second book", secondBookAuthors, secondBookGenres);
        book2.setId(2L);
        Book book3 = new Book("Third book", thirdBookAuthors, thirdBookGenres);
        book3.setId(3L);

        allBooks = Arrays.asList(book1, book2, book3);
        firstGenreBooks = Arrays.asList(book1, book3);
        firstAuthorBooks = Arrays.asList(book1, book3);
        firstTitleBooks = Arrays.asList(book1);
    }

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(bookController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    }

    @Test
    public void getEmptyBooksList() throws Exception {
        // Given
        given(bookService.findAll()).willReturn(new ArrayList<Book>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getNotEmptyBooksList() throws Exception {
        // Expected json
        JsonContent<List<Book>> jsonBookContent = jsonBooks.write(allBooks);

        // Given
        given(bookService.findAll()).willReturn(allBooks);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonBookContent.getJson());
    }

    @Test
    public void getNotEmptyBooksListFilteredByGenre() throws Exception {
        // Expected json
        JsonContent<List<Book>> jsonBookContent = jsonBooks.write(firstGenreBooks);

        // Given
        given(bookService.findAllByGenresContainingName("genre1")).willReturn(firstGenreBooks);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books").param("genre", "genre1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonBookContent.getJson());
    }

    @Test
    public void getNotEmptyBooksListFilteredByTitle() throws Exception {
        // Expected json
        JsonContent<List<Book>> jsonBookContent = jsonBooks.write(firstTitleBooks);

        // Given
        given(bookService.findAllByTitleContaining("First")).willReturn(firstTitleBooks);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books").param("title", "First")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonBookContent.getJson());
    }

    @Test
    public void getNotEmptyBooksListFilteredByAuthor() throws Exception {
        // Expected json
        JsonContent<List<Book>> jsonBookContent = jsonBooks.write(firstAuthorBooks);

        // Given
        given(bookService.findAllByAuthorsContainingName("author1")).willReturn(firstAuthorBooks);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books").param("author", "author1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonBookContent.getJson());
    }

    @Test
    public void getExistingBookWorks() throws Exception {
        Book book = allBooks.get(0);

        // Expected json
        JsonContent<Book> bookJsonContent = jsonBook.write(book);

        // Given
        given(bookService.findById(1L)).willReturn(book);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(bookJsonContent.getJson());
    }

    @Test
    public void getNotExistingBookHandlerWorks() throws Exception {
        // Given
        given(bookService.findById(1L)).willThrow(new ResourceNotFoundException("Book with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Book with this id not found");
    }

    @Test
    public void newBookNullValuesAreHandled() throws Exception {
        NewBookDto newBookDto = new NewBookDto();

        JsonContent<NewBookDto> bookDtoJsonContent = jsonBookDto.write(newBookDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/books/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("title must not be null");
        assertThat(response.getContentAsString()).contains("authorIds must not be null");
        assertThat(response.getContentAsString()).contains("genreIds must not be null");
    }

    @Test
    public void newBookInvalidFieldSizesAreHandled() throws Exception {
        NewBookDto newBookDto = new NewBookDto();
        newBookDto.setTitle("");
        newBookDto.setAuthorIds(new ArrayList<>());
        newBookDto.setGenreIds(new ArrayList<>());

        JsonContent<NewBookDto> bookDtoJsonContent = jsonBookDto.write(newBookDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/books/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("title length must be between 1 and 255");
        assertThat(response.getContentAsString()).contains("authorIds size must be between 1 and 10");
        assertThat(response.getContentAsString()).contains("genreIds size must be between 1 and 10");
    }

    @Test
    public void newBookCreationFailsWhenAuthorIdDoesNotExist() throws Exception {
        NewBookDto newBookDto = new NewBookDto();
        newBookDto.setTitle("Test title of this book");
        newBookDto.setAuthorIds(new ArrayList<Long>(Arrays.asList(1L, 2L, 3L)));
        newBookDto.setGenreIds(new ArrayList<Long>(Arrays.asList(1L)));

        JsonContent<NewBookDto> bookDtoJsonContent = jsonBookDto.write(newBookDto);

        // Given
        given(authorService.findById(anyLong())).willThrow(new ResourceNotFoundException("Author with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/books/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Author with this id not found");
    }

    @Test
    public void newBookCreationFailsWhenGenreIdDoesNotExist() throws Exception {
        NewBookDto newBookDto = new NewBookDto();
        newBookDto.setTitle("Test title of this book");
        newBookDto.setAuthorIds(new ArrayList<Long>(Arrays.asList(1L, 2L, 3L)));
        newBookDto.setGenreIds(new ArrayList<Long>(Arrays.asList(1L)));

        JsonContent<NewBookDto> bookDtoJsonContent = jsonBookDto.write(newBookDto);

        // Given
        given(authorService.findById(anyLong())).willReturn(new Author("Test author name", "Test author description"));
        given(genreService.findById(1L)).willThrow(new ResourceNotFoundException("Genre with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/books/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Genre with this id not found");
    }

    @Test
    public void newBookIsSavedCorrectly() throws Exception {
        // Sent json
        NewBookDto newBookDto = new NewBookDto();
        newBookDto.setTitle("First book");
        newBookDto.setAuthorIds(new ArrayList<Long>(Arrays.asList(1L)));
        newBookDto.setGenreIds(new ArrayList<Long>(Arrays.asList(1L)));

        JsonContent<NewBookDto> bookDtoJsonContent = jsonBookDto.write(newBookDto);

        // After "saving"
        Book savedBook = allBooks.get(0); // any working book

        // Expected json
        JsonContent<Book> bookJsonContent = jsonBook.write(savedBook);

        // Given
        given(bookService.save(any(Book.class))).willReturn(savedBook);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(bookJsonContent.getJson());
    }

    @Test
    public void patchNotExistingBookHandlerWorks() throws Exception {
        PatchBookDto patchBookDto = new PatchBookDto();
        patchBookDto.setTitle("Test");
        patchBookDto.setAuthorIds(Arrays.asList(1L, 2L));
        patchBookDto.setGenreIds(Arrays.asList(1L, 2L));

        JsonContent<PatchBookDto> patchBookDtoJsonContent = jsonPatchBookDto.write(patchBookDto);

        // Given
        given(bookService.findById(1L)).willThrow(new ResourceNotFoundException("Book with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(patchBookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Book with this id not found");
    }

    @Test
    public void patchBookInvalidFieldLengthsAreHandled() throws Exception {
        PatchBookDto patchBookDto = new PatchBookDto();
        patchBookDto.setTitle("");
        patchBookDto.setAuthorIds(new ArrayList<>());
        patchBookDto.setGenreIds(new ArrayList<>());

        JsonContent<PatchBookDto> patchBookDtoJsonContent = jsonPatchBookDto.write(patchBookDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(patchBookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("genreIds size must be between 1 and 10");
        assertThat(response.getContentAsString()).contains("title length must be between 1 and 255");
        assertThat(response.getContentAsString()).contains("authorIds size must be between 1 and 10");
    }

    @Test
    public void patchBookReturnsPatchedBookContents() throws Exception {
        PatchBookDto patchBookDto = new PatchBookDto();
        patchBookDto.setTitle("Test title");

        // Sent json
        JsonContent<PatchBookDto> patchBookDtoJsonContent = jsonPatchBookDto.write(patchBookDto);

        // Initial book
        Book book = allBooks.get(0);

        // "Saved book"
        Book savedBook = allBooks.get(0);
        savedBook.setTitle(patchBookDto.getTitle());

        // Expected json
        JsonContent<Book> bookJsonContent = jsonBook.write(savedBook);

        // Given
        given(bookService.findById(1L)).willReturn(book);
        given(bookService.save(book)).willReturn(savedBook);

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(patchBookDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(bookJsonContent.getJson());
    }

    @Test
    public void deleteBookDeletesExistingBook() throws Exception {
        // Given
        given(bookService.deleteById(1L)).willReturn(true);

        // When
        MockHttpServletResponse response = mvc.perform(
                delete("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("{\"deleted\":true}");
    }

    @Test
    public void deleteNotExistingBookHandlerWorks() throws Exception {
        // Given
        given(bookService.deleteById(1L)).willThrow(new ResourceNotFoundException("Book with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                delete("/api/books/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Book with this id not found");
    }


}
