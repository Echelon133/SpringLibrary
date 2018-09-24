package ml.echelon133.register;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.register.exception.NewUserValidationException;
import ml.echelon133.register.exception.UsernameAlreadyTakenException;
import ml.echelon133.security.secret.ISecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
public class RegistrationController {

    private IUserService userService;
    private IAuthorityService authorityService;
    private ISecretGenerator secretGenerator;

    @Autowired
    public RegistrationController(IUserService userService, IAuthorityService authorityService, ISecretGenerator secretGenerator) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.secretGenerator = secretGenerator;
    }

    @RequestMapping(value = "users/register", method = RequestMethod.POST)
    public ResponseEntity<Map> registerUser(@Valid @RequestBody NewUserDto newUserDto, BindingResult result)
            throws NewUserValidationException, UsernameAlreadyTakenException {

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            List<ObjectError> objectErrors = result.getGlobalErrors();
            throw new NewUserValidationException(fieldErrors, objectErrors);
        }

        String username = newUserDto.getUsername();
        String password = newUserDto.getPassword();
        Authority authority;

        try {
            authority = authorityService.findByAuthority("ROLE_USER");
        } catch (ResourceNotFoundException ex) {
            // default authority not found, so we have to create it
            authority = new Authority("ROLE_USER");
        }

        try {
            User user = userService.findUserByUsername(username);
        } catch (ResourceNotFoundException ex) {
            // username given by the user can be taken, so we proceed
            Map<String, Boolean> response = new HashMap<>();

            User user = new User();
            user.setPassword(password);
            user.setUsername(username);
            user.setAuthorities(new HashSet<>(Arrays.asList(authority)));
            user.setSecret(secretGenerator.generateSecret());

            User savedUser = userService.save(user);
            if (savedUser != null) {
                response.put("isRegistered", true);
            } else {
                response.put("isRegistered", false);
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        throw new UsernameAlreadyTakenException("This username is already taken");
    }
}
