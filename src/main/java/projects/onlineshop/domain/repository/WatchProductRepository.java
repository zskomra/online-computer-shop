package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.WatchProduct;

public interface WatchProductRepository extends JpaRepository<WatchProduct, Long> {

}
