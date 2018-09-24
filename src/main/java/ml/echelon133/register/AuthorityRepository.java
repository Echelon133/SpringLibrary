package ml.echelon133.register;

import ml.echelon133.register.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findAuthorityByAuthority(String authorityName);
}
