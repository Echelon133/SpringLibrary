package ml.echelon133.repository;

import ml.echelon133.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    @Query("Select u.secret from User u where u.username=:username")
    String findSecretByUsername(@Param("username")String username);
}
