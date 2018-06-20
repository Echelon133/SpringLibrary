package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.model.Genre;
import ml.echelon133.service.IGenreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class GenreControllerTest {

    private MockMvc mvc;

    @Mock
    private IGenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private JacksonTester<List<Genre>> jsonGenre;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    @Test
    public void getEmptyGenresList() throws Exception {
        // Given
        given(genreService.findAll()).willReturn(new ArrayList<Genre>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/api/genres")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
