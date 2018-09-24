package ml.echelon133.token;

import ml.echelon133.exception.APIExceptionHandler;
import ml.echelon133.token.FailedTokenGenerationException;
import ml.echelon133.message.ErrorMessage;
import ml.echelon133.message.IErrorMessage;
import ml.echelon133.token.ITokenService;
import ml.echelon133.token.TokenController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TokenControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private TokenController tokenController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(tokenController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    };

    @Test
    public void failedTokenGenerationHandlerWorks() throws Exception {
        Principal testPrincipal = new Principal() {
            @Override
            public String getName() {
                return "test-user";
            }
        };

        // Given
        given(tokenService.generateTokenForUser("test-user"))
                .willThrow(new FailedTokenGenerationException("Token could not be generated"));

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/users/get-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(testPrincipal)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Token could not be generated");
    }

    @Test
    public void tokenResponseIsDisplayedCorrectly() throws Exception {
        Principal testPrincipal = new Principal() {
            @Override
            public String getName() {
                return "valid-user";
            }
        };

        // Given
        given(tokenService.generateTokenForUser("valid-user")).willReturn("this-is-a-test-token");

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/users/get-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(testPrincipal)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"token\":\"this-is-a-test-token\"}");
    }
}
