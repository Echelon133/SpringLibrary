package ml.echelon133.service;

import ml.echelon133.exception.ResourceNotFoundException;
import ml.echelon133.model.Authority;
import ml.echelon133.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AuthorityService implements IAuthorityService {

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findByAuthority(String authority) throws ResourceNotFoundException {
        Authority auth = authorityRepository.findAuthorityByAuthority(authority);
        if (auth == null) {
            throw new ResourceNotFoundException("Authority with this name not found")
        }
        return auth;
    }

    @Override
    public Authority findById(Long id) throws ResourceNotFoundException {
        Optional<Authority> authority = authorityRepository.findById(id);
        if (authority.isPresent()) {
            return authority.get();
        } else {
            throw new ResourceNotFoundException("Authority with this id not found");
        }
    }
}
