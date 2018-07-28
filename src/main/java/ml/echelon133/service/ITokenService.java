package ml.echelon133.service;

import ml.echelon133.exception.FailedTokenGenerationException;

public interface ITokenService {
    String generateTokenForUser(String username) throws FailedTokenGenerationException;
    String extractUsernameFromToken(String token);
    Boolean isValidToken(String token);
}
