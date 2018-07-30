package ml.echelon133.security.jwt;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class JWTAuthenticationManager implements AuthenticationManager {

    private AuthenticationProvider authenticationProvider;

    public JWTAuthenticationManager(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.authenticationProvider.authenticate(authentication);
    }
}
