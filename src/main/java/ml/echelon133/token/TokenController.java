package ml.echelon133.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

    private ITokenService tokenService;

    @Autowired
    public TokenController(ITokenService tokenService) {
        this.tokenService = tokenService;
    }


    @RequestMapping(value = "users/get-token", method = RequestMethod.GET)
    public ResponseEntity<Map> getToken(Principal principal) throws FailedTokenGenerationException {
        String username = principal.getName();
        String generatedToken = tokenService.generateTokenForUser(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", generatedToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
