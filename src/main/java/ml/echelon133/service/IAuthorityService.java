package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Authority;

public interface IAuthorityService {
    Authority findByAuthority(String authority) throws ResourceNotFoundException;
    Authority findById(Long id) throws ResourceNotFoundException;
}
