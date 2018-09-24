package ml.echelon133.register;

import ml.echelon133.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
            throw new ResourceNotFoundException("Authority with this name not found");
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
