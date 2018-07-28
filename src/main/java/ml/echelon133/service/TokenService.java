package ml.echelon133.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import ml.echelon133.exception.FailedTokenGenerationException;
import ml.echelon133.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public class TokenService implements ITokenService {

    private IUserService userService;

    @Autowired
    public TokenService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateTokenForUser(String username) throws FailedTokenGenerationException {
        String secret;
        Algorithm algorithm;
        String generatedToken;

        try {
            secret = userService.findSecretByUsername(username);
        } catch (ResourceNotFoundException ex) {
            // we do not have the secret of a specific user, we cannot proceed
            throw new FailedTokenGenerationException("Token could not be generated, because the secret was null");
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 3);
        Date threeDaysFromNow = cal.getTime();

        algorithm = Algorithm.HMAC512(secret);
        generatedToken = JWT
                .create()
                .withClaim("username", username)
                .withIssuer("library-app")
                .withExpiresAt(threeDaysFromNow)
                .sign(algorithm);

        return generatedToken;
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return null;
    }

    @Override
    public Boolean isValidToken(String token) {
        return null;
    }
}
