package ml.echelon133.register;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.register.User;
import ml.echelon133.register.IUserService;
import ml.echelon133.register.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            String plainPassword = user.getPassword();
            String encodedPassword = passwordEncoder.encode(plainPassword);
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User with this username not found");
        }
        return user;
    }

    @Override
    public User findById(Long id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ResourceNotFoundException("User with this id not found");
        }
    }

    @Override
    public String findSecretByUsername(String username) throws ResourceNotFoundException {
        String secret = userRepository.findSecretByUsername(username);
        if (secret == null) {
            throw new ResourceNotFoundException("User with this username does not exist, therefore secret was not found");
        } else {
            return secret;
        }
    }
}
