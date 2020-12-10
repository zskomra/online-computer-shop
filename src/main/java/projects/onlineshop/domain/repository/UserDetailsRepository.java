package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
