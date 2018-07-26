package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Authority;
import ml.echelon133.model.User;
import ml.echelon133.model.dto.NewUserDto;
import ml.echelon133.model.message.ErrorMessage;
import ml.echelon133.model.message.IErrorMessage;
import ml.echelon133.security.secret.ISecretGenerator;
import ml.echelon133.service.IAuthorityService;
import ml.echelon133.service.IUserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    private MockMvc mvc;

    @Mock
    private WebApplicationContext context;

    @Mock
    private IUserService userService;

    @Mock
    private IAuthorityService authorityService;

    @Mock
    private ISecretGenerator secretGenerator;

    @InjectMocks
    private RegistrationController registrationController;

    @InjectMocks
    private APIExceptionHandler exceptionHandler;

    private JacksonTester<NewUserDto> jsonNewUserDto;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(registrationController).setControllerAdvice(exceptionHandler).build();

        given(context.getBean(IErrorMessage.class)).willReturn(new ErrorMessage());
    }

    @Test
    public void newUserPasswordsMatchValidatorWorks() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("Test");
        newUserDto.setPassword("firstPassword");
        newUserDto.setPassword2("secondPassword");

        JsonContent<NewUserDto> newUserDtoJsonContent = jsonNewUserDto.write(newUserDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserDtoJsonContent.getJson())
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Passwords do not match");
    }

    @Test
    public void newUserInvalidFieldLengthsAreHandled() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("Te");
        newUserDto.setPassword("pass");
        newUserDto.setPassword2("pass");

        JsonContent<NewUserDto> newUserDtoJsonContent = jsonNewUserDto.write(newUserDto);

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserDtoJsonContent.getJson())
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("username length must be between 4 and 50");
        assertThat(response.getContentAsString()).contains("password length must be between 6 and 100");
    }

    @Test
    public void newUserUsernameAlreadyTakenHandler() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("TestUser");
        newUserDto.setPassword("password321");
        newUserDto.setPassword2("password321");

        JsonContent<NewUserDto> newUserDtoJsonContent = jsonNewUserDto.write(newUserDto);

        // Given
        given(authorityService.findByAuthority("ROLE_USER")).willReturn(new Authority("ROLE_USER"));
        given(userService.findUserByUsername(newUserDto.getUsername())).willReturn(new User()); // return any not null value

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserDtoJsonContent.getJson())
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getContentAsString()).contains("This username is already taken");
    }

    @Test
    public void newUserWithValidDataCanBeRegistered() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setUsername("ValidUser");
        newUserDto.setPassword("password321");
        newUserDto.setPassword2("password321");

        JsonContent<NewUserDto> newUserDtoJsonContent = jsonNewUserDto.write(newUserDto);

        // Given
        given(authorityService.findByAuthority("ROLE_USER")).willReturn(new Authority("ROLE_USER"));
        given(userService.findUserByUsername(newUserDto.getUsername()))
                .willThrow(new ResourceNotFoundException("User with this username not found"));
        given(secretGenerator.generateSecret()).willReturn("my_s3cr3t_v4lu3");
        given(userService.save(any(User.class))).willReturn(new User());

        // When
        MockHttpServletResponse response = mvc.perform(
                post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserDtoJsonContent.getJson())
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"isRegistered\":true}");
    }
}
