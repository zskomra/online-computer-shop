package projects.onlineshop.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);
    Product getById(Long id);
    Product getProductsByName(String name);
    Page<Product> findAllByNameContaining(Pageable pageable, String regex);

    List<Product> findTop5ByOrderByAddDateDesc ();
    List<Product> getAllByCategoryId(Long productCategoryId);

    @Query(value = "SELECT  pr.product, avg (pr.rating) from ProductRating pr group by pr.product order by avg(pr.rating)desc " )
    List<Product> findTop5RatedProducts(Pageable pageable);


}
