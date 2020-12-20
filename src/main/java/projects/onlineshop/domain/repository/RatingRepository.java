package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.ProductRating;

public interface RatingRepository extends JpaRepository<ProductRating, Long> {

}
