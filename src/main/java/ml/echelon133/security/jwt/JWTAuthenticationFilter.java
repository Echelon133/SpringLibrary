package ml.echelon133.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String AUTH_HEADER_NAME = "Authorization";
    private boolean continueChainBeforeSuccessfulAuthentication = false;

    public JWTAuthenticationFilter() {
        super(new AntPathRequestMatcher("/"));
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean requires = false;

        if (auth == null || !auth.isAuthenticated()) {
            String header = request.getHeader(AUTH_HEADER_NAME);
            try {
                Boolean requestMatches = super.requiresAuthentication(request, response);
                Boolean headerCorrect = header.startsWith("Bearer");
                if (headerCorrect && requestMatches) {
                    requires = true;
                }
            } catch (NullPointerException ex) {
                requires = false;
            }
        } else {
            requires = false;
        }
        return requires;
    }

    @Override
    public void setContinueChainBeforeSuccessfulAuthentication(boolean continueChainBeforeSuccessfulAuthentication) {
        this.continueChainBeforeSuccessfulAuthentication = continueChainBeforeSuccessfulAuthentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (this.continueChainBeforeSuccessfulAuthentication) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        String token = httpServletRequest.getHeader(AUTH_HEADER_NAME);
        JWTToken auth = new JWTToken(token);
        return getAuthenticationManager().authenticate(auth);
    }
}
