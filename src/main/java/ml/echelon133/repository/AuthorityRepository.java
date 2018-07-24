package ml.echelon133.repository;

import ml.echelon133.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findAuthorityByAuthority(String authorityName);
}
