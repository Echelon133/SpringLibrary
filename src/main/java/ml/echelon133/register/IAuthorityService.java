package ml.echelon133.register;

import ml.echelon133.exception.ResourceNotFoundException;

public interface IAuthorityService {
    Authority findByAuthority(String authority) throws ResourceNotFoundException;
    Authority findById(Long id) throws ResourceNotFoundException;
}
