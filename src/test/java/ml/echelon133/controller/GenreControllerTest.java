package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Genre;
import ml.echelon133.model.dto.GenreDto;
import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.service.IGenreService;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
public class GenreControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private IGenreService genreService;

    @InjectMocks
    private GenreController genreController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    private JacksonTester<List<Genre>> jsonGenres;

    private JacksonTester<GenreDto> jsonGenreDto;

    private JacksonTester<Genre> jsonGenre;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(genreController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    }

    @Test
    public void getEmptyGenresList() throws Exception {
        // Given
        given(genreService.findAll()).willReturn(new ArrayList<Genre>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getNotEmptyGenresList() throws Exception {
        // Prepare test Genres
        Genre testGenre1 = new Genre("Test genre", "Test description");
        Genre testGenre2 = new Genre("Test genre2", "Test description2");
        List<Genre> genres = Arrays.asList(testGenre1, testGenre2);

        // Prepare expected JSON
        JsonContent<List<Genre>> jsonGenreContent = jsonGenres.write(genres);

        // Given
        given(genreService.findAll()).willReturn(genres);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonGenreContent.getJson());
    }

    @Test
    public void getNotExistingGenreHandlerWorks() throws Exception {
        // Given
        given(genreService.findById(1L)).willThrow(new ResourceNotFoundException("Genre with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Genre with this id not found");
    }

    @Test
    public void newGenreNullValuesAreHandled() throws Exception {
        GenreDto genreDto = new GenreDto(null, null);

        // Prepare json
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("name must not be null");
        assertThat(response.getContentAsString()).contains("description must not be null");
    }

    @Test
    public void newGenreInvalidFieldLengthsAreHandled() throws Exception {
        GenreDto genreDto = new GenreDto("", "test");

        // Prepare json
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("name length must be between 1 and 50");
        assertThat(response.getContentAsString()).contains("description length must be between 10 and 1500");
    }

    @Test
    public void newGenreIsSavedCorrectly() throws Exception {
        // Sent json
        GenreDto genreDto = new GenreDto("test name", "test description of this genre");
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // After "saving"
        Genre savedGenre = new Genre("test name", "test description of this genre");
        savedGenre.setId(1L);

        // Expected json
        JsonContent<Genre> genreJsonContent = jsonGenre.write(savedGenre);

        // Given
        given(genreService.save(any(Genre.class))).willReturn(savedGenre);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(genreJsonContent.getJson());
    }

    @Test
    public void patchNotExistingGenreHandlerWorks() throws Exception {
        GenreDto genreDto = new GenreDto("test genre", "test description of the genre");
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // Given
        given(genreService.findById(1L)).willThrow(new ResourceNotFoundException("Genre with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Genre with this id not found");
    }

    @Test
    public void patchGenreNullValuesAreHandled() throws Exception {
        GenreDto genreDto = new GenreDto(null, null);
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("name must not be null");
        assertThat(response.getContentAsString()).contains("description must not be null");
    }

    @Test
    public void patchGenreInvalidFieldLengthsAreHandled() throws Exception {
        GenreDto genreDto = new GenreDto("", "test");
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("name length must be between 1 and 50");
        assertThat(response.getContentAsString()).contains("description length must be between 10 and 1500");
    }

    @Test
    public void patchGenreIsSavedCorrectly() throws Exception {
        // Sent json
        GenreDto genreDto = new GenreDto("test name", "test description of this genre");
        JsonContent<GenreDto> genreDtoJsonContent = jsonGenreDto.write(genreDto);

        // Before "saving"
        Genre genre = new Genre("initial name", "test description of this genre");
        genre.setId(1L);

        // After "saving"
        Genre savedGenre = new Genre(genreDto.getName(), genreDto.getDescription());
        savedGenre.setId(1L);

        // Expected json
        JsonContent<Genre> genreJsonContent = jsonGenre.write(savedGenre);

        // Given
        given(genreService.findById(1L)).willReturn(genre);
        given(genreService.save(genre)).willReturn(savedGenre);

        // When
        MockHttpServletResponse response = mvc.perform(
                patch("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(genreDtoJsonContent.getJson())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(genreJsonContent.getJson());
    }

    @Test
    public void deleteGenreDeletesExistingGenre() throws Exception {
        // Given
        given(genreService.deleteById(1L)).willReturn(true);

        // When
        MockHttpServletResponse response = mvc.perform(
                delete("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("{\"deleted\":true}");
    }

    @Test
    public void deleteNotExistingGenreHandlerWorks() throws Exception {
        // Given
        given(genreService.deleteById(1L)).willThrow(new ResourceNotFoundException("Genre with this id not found"));

        // When
        MockHttpServletResponse response = mvc.perform(
                delete("/api/genres/1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains("Genre with this id not found");
    }
}
