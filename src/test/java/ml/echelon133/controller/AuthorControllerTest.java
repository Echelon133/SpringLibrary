package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.model.Author;
import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.service.IAuthorService;
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

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private IAuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    private JacksonTester<List<Author>> jsonAuthors;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(authorController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    }

    @Test
    public void getEmptyAuthorsList() throws Exception {
        // Given
        given(authorService.findAll()).willReturn(new ArrayList<>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getNotEmptyAuthorsList() throws Exception {
        Author author1 = new Author("First Author", "First Author description");
        Author author2 = new Author("Second Author", "Second Author description");
        List<Author> authors = Arrays.asList(author1, author2);

        // Expected Json
        JsonContent<List<Author>> jsonAuthorContent = jsonAuthors.write(authors);

        // Given
        given(authorService.findAll()).willReturn(authors);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonAuthorContent.getJson());
    }

    @Test
    public void getFilteredAuthorsList() throws Exception {
        Author author1 = new Author("John First", "John First description");
        author1.setId(1L);
        Author author2 = new Author("John Third", "John Third description");
        author2.setId(2L);
        List<Author> expectedAuthors = Arrays.asList(author1, author2);

        // Expected json
        JsonContent<List<Author>> jsonAuthorContent = jsonAuthors.write(expectedAuthors);

        // Given
        given(authorService.findAllByNameContaining("John")).willReturn(expectedAuthors);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", "John")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonAuthorContent.getJson());
    }

}
