package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order getByUserUsername(String username);


}
