package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.User;

public interface IUserService {
    User save(User user);
    User findUserByUsername(String username) throws ResourceNotFoundException;
    User findById(Long id) throws ResourceNotFoundException;
}
