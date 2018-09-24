package ml.echelon133.token;

public interface ITokenService {
    String generateTokenForUser(String username) throws FailedTokenGenerationException;
    String extractUsernameFromToken(String token);
    Boolean isValidToken(String token);
}
