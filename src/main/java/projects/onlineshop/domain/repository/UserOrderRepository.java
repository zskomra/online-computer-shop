package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.order.UserOrders;

public interface UserOrderRepository extends JpaRepository<UserOrders, Long> {

}
