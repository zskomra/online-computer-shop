package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.ProductRating;

public interface RatingRepository extends JpaRepository<ProductRating, Long> {

    @Query("SELECT pr.id FROM ProductRating pr JOIN Product p ON p.id = pr.product.id JOIN User u ON u.id = pr.user.id")
    boolean getIsDuplicateRating();
}
