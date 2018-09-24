package ml.echelon133.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ml.echelon133.token.FailedTokenGenerationException;
import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.register.IUserService;
import ml.echelon133.token.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private TokenService tokenService;

    private String generateTestToken(String username, String issuer, Date expiresAt, String secret) {
        String generatedToken;

        Algorithm algorithm;
        algorithm = Algorithm.HMAC512(secret);
        generatedToken = JWT
                .create()
                .withClaim("username", username)
                .withIssuer(issuer)
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return generatedToken;
    }


    @Test(expected = FailedTokenGenerationException.class)
    public void tokenGenerationFailsIfSecretIsNull() throws Exception {
        // Given
        given(userService.findSecretByUsername("notExistentUser"))
                .willThrow(new ResourceNotFoundException("User with this username does not exist, therefore secret was not found"));

        // When
        tokenService.generateTokenForUser("notExistentUser");
    }

    @Test
    public void generatedTokenValidatesCorrectly() throws Exception {
        // Given
        given(userService.findSecretByUsername("testUser")).willReturn("aaaabbbbccccdddd"); // simple, but ok for tests

        // When
        String generatedToken = tokenService.generateTokenForUser("testUser");
        Boolean isTokenValid = tokenService.isValidToken(generatedToken);

        // Then
        assertThat(isTokenValid).isTrue();
    }

    @Test
    public void generatedTokenWithBearerPrefixValidatesCorrectly() throws Exception {
        // Given
        given(userService.findSecretByUsername("testUser")).willReturn("aaaabbbbccccdddd"); // simple, but ok for tests

        // When
        String generatedToken = tokenService.generateTokenForUser("testUser");
        generatedToken = "Bearer " + generatedToken;
        Boolean isTokenValid = tokenService.isValidToken(generatedToken);

        // Then
        assertThat(isTokenValid).isTrue();
    }

    @Test
    public void tokenValidationFailsWhenSecretIsInvalid() throws Exception {
        // Given
        given(userService.findSecretByUsername("some-username")).willReturn("aaaazzzzccccdddd");

        // When
        String generatedToken = generateTestToken("some-username", "library-app", new Date(), "aaaabbbbccccdddd");
        Boolean isTokenValid = tokenService.isValidToken(generatedToken);

        // Then
        assertThat(isTokenValid).isFalse();
    }

    @Test
    public void tokenValidationFailsWhenTokenHasExpired() throws Exception {
        // Given
        given(userService.findSecretByUsername("some-username")).willReturn("aaaabbbbccccdddd");

        // When
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        Date expiresAt = cal.getTime();

        // create an expired token
        String generatedToken = generateTestToken("some-username", "library-app", expiresAt, "aaaabbbbccccdddd");
        Boolean isTokenValid = tokenService.isValidToken(generatedToken);

        // Then
        assertThat(isTokenValid).isFalse();
    }

    @Test
    public void tokenValidationFailsWhenIssuerIsInvalid() throws Exception {
        // Given
        given(userService.findSecretByUsername("some-username")).willReturn("aaaabbbbccccdddd");

        // When
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 3);
        Date expiresAt = cal.getTime();

        // invalid issuer
        String generatedToken = generateTestToken("some-username", "other-app", expiresAt, "aaaabbbbccccdddd");
        Boolean isTokenValid = tokenService.isValidToken(generatedToken);

        // Then
        assertThat(isTokenValid).isFalse();
    }
}
