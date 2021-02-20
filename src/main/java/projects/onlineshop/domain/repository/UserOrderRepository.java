package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projects.onlineshop.domain.model.order.UserOrder;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> getAllByUserIdOrderByOrderDateDesc(Long userId);

}
