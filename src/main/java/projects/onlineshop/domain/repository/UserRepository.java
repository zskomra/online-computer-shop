package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);


    User getUsersByUsername(String username);

    Optional<User> findByUsername(String username);


}
