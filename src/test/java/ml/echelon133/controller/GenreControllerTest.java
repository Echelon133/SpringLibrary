package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Genre;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    private JacksonTester<List<Genre>> jsonGenre;


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
        JsonContent<List<Genre>> jsonGenreContent = jsonGenre.write(genres);

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
}
