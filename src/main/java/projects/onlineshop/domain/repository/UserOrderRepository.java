package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.order.UserOrder;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

}
