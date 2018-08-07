package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.*;
import ml.echelon133.model.dto.NewEntryDto;
import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.service.IBookService;
import ml.echelon133.service.IEntryService;
import ml.echelon133.service.IUserService;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class EntryControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private IEntryService entryService;

    @Mock
    private IBookService bookService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private EntryController entryController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    private JacksonTester<List<Entry>> jsonEntries;
    private JacksonTester<NewEntryDto> jsonNewEntryDto;
    private JacksonTester<Entry> jsonEntry;

    private static List<Entry> testEntries;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(entryController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    }

    @BeforeClass
    public static void beforeSetup() {
        Genre genre1 = new Genre("Test genre1", "Test genre1 description");
        Genre genre2 = new Genre("Test genre2", "Test genre2 description");

        Author author1 = new Author("Test author1", "Test author1 description");
        Author author2 = new Author("Test author2", "Test author2 description");

        User user1 = new User();
        user1.setUsername("user1");
        user1.setId(1L);

        Book book1 = new Book("Test book1", new HashSet<>(Arrays.asList(author1)), Arrays.asList(genre1));
        Book book2 = new Book("Test book2", new HashSet<>(Arrays.asList(author1, author2)), Arrays.asList(genre2));
        Book book3 = new Book("Test book3", new HashSet<>(Arrays.asList(author2)), Arrays.asList(genre1, genre2));

        Entry entry1 = new Entry(book1, user1);
        Entry entry2 = new Entry(book2, user1);
        Entry entry3 = new Entry(book3, user1);

        entry3.returnBook();

        testEntries = Arrays.asList(entry1, entry2, entry3);
    }

    @Test
    public void getEmptyEntriesList() throws Exception {
        // Given
        given(entryService.findAll()).willReturn(new ArrayList<>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getEntriesFilteredByReturnedStatus() throws Exception {
        List<Entry> returnedEntries = testEntries.stream().filter(e -> e.getReturned()).collect(Collectors.toList());

        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(returnedEntries);

        // Given
        given(entryService.findAllByReturned(true)).willReturn(returnedEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .param("returned", "true")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void getEntriesFilteredByBookTitle() throws Exception {
        List<Entry> filteredByTitleEntries = testEntries
                .stream()
                .filter(e -> e.getBookBorrowed().getTitle().contains("Test"))
                .collect(Collectors.toList());

        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(filteredByTitleEntries);

        // Given
        given(entryService.findAllByBookTitleContaining("Test")).willReturn(filteredByTitleEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .param("bookTitle", "Test")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void getEntriesFilteredByUsername() throws Exception {
        List<Entry> filteredByUsernameEntries = testEntries
                .stream()
                .filter(e -> e.getUserBorrowing().getUsername().equals("user1"))
                .collect(Collectors.toList());

        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(filteredByUsernameEntries);

        // Given
        given(entryService.findAllByUsername("user1")).willReturn(filteredByUsernameEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .param("username", "user1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void getEntriesFilteredByAddedSince() throws Exception {
        Date weekAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7));
        List<Entry> filteredByAddedSinceEntries = testEntries
                .stream()
                .filter(e -> e.getDateStarted().after(weekAgo))
                .collect(Collectors.toList());

        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(filteredByAddedSinceEntries);

        // Given
        given(entryService.findAllByAddedSince(IEntryService.AddedSince.WEEK)).willReturn(filteredByAddedSinceEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .param("since", "week")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void getAllEntriesIfAddedSinceParameterIsWrong() throws Exception {
        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(testEntries);

        // Given
        given(entryService.findAll()).willReturn(testEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .param("since", "asdf")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void getAllEntries() throws Exception {
        // Expected json
        JsonContent<List<Entry>> jsonEntriesContent = jsonEntries.write(testEntries);

        // Given
        given(entryService.findAll()).willReturn(testEntries);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEntriesContent.getJson());
    }

    @Test
    public void newEntryNullValuesAreHandled() throws Exception {
        NewEntryDto newEntryDto = new NewEntryDto();

        JsonContent<NewEntryDto> entryDtoJsonContent = jsonNewEntryDto.write(newEntryDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(entryDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("borrowerUsername must not be null");
        assertThat(response.getContentAsString()).contains("borrowedBookId must not be null");
    }

    @Test
    public void newEntryBorrowedBookIdIsValidated() throws Exception {
        NewEntryDto newEntryDto = new NewEntryDto();
        newEntryDto.setBorrowedBookId(-1L);
        newEntryDto.setBorrowerUsername("test-username");

        JsonContent<NewEntryDto> entryDtoJsonContent = jsonNewEntryDto.write(newEntryDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(entryDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("borrowedBookId must be greater than 0");
    }

    @Test
    public void newEntryCreationFailsIfBookDoesNotExist() throws Exception {
        NewEntryDto newEntryDto = new NewEntryDto();
        newEntryDto.setBorrowedBookId(1L);
        newEntryDto.setBorrowerUsername("test-username");

        JsonContent<NewEntryDto> entryDtoJsonContent = jsonNewEntryDto.write(newEntryDto);

        // Given
        given(bookService.findById(1L)).willThrow(new ResourceNotFoundException("Book with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(entryDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Book with this id not found");
    }

    @Test
    public void newEntryCreationFailsIfUsernameNotFound() throws Exception {
        NewEntryDto newEntryDto = new NewEntryDto();
        newEntryDto.setBorrowedBookId(1L);
        newEntryDto.setBorrowerUsername("test-username");

        JsonContent<NewEntryDto> entryDtoJsonContent = jsonNewEntryDto.write(newEntryDto);

        // Given
        given(userService.findUserByUsername("test-username")).willThrow(new ResourceNotFoundException("User with this username not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(entryDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("User with this username not found");
    }

    @Test
    public void newEntryIsSavedCorrectly() throws Exception {
        NewEntryDto newEntryDto = new NewEntryDto();
        newEntryDto.setBorrowedBookId(1L);
        newEntryDto.setBorrowerUsername("user1");

        JsonContent<NewEntryDto> entryDtoJsonContent = jsonNewEntryDto.write(newEntryDto);

        // Expected json
        JsonContent<Entry> entryJsonContent = jsonEntry.write(testEntries.get(0));

        // Given
        given(entryService.save(any(Entry.class))).willReturn(testEntries.get(0));

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/entries")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(entryDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(entryJsonContent.getJson());
    }

    @Test
    public void getExistingEntryWorks() throws Exception {
        Entry entry = testEntries.get(0);

        JsonContent<Entry> entryJsonContent = jsonEntry.write(entry);

        // Given
        given(entryService.findById(1L)).willReturn(entry);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(entryJsonContent.getJson());
    }

    @Test
    public void getNotExistingEntryHandlerWorks() throws Exception {
        // Given
        given(entryService.findById(1L)).willThrow(new ResourceNotFoundException("Entry with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/entries/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Entry with this id not found");
    }
}
