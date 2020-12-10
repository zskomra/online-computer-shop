package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.ProductCategory;


public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByName(String name);

    ProductCategory getOneByName(String name);
}
