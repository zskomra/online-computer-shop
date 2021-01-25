package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;

import java.util.List;

public interface RatingRepository extends JpaRepository<ProductRating, Long> {

}
