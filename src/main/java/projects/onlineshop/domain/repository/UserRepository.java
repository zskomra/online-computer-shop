package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.User;


public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    User getUsersByUsername(String username);
}
