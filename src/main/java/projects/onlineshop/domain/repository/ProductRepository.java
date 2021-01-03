package projects.onlineshop.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);
    Product getById(Long id);
    Page<Product> findAllByNameContaining(Pageable pageable, String regex);


    List<Product> getAllByCategoryId(Long productCategoryId);

}
