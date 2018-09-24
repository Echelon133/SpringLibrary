package ml.echelon133.register;

import ml.echelon133.exception.ResourceNotFoundException;

public interface IUserService {
    User save(User user);
    User findUserByUsername(String username) throws ResourceNotFoundException;
    User findById(Long id) throws ResourceNotFoundException;
    String findSecretByUsername(String username) throws ResourceNotFoundException;
}
