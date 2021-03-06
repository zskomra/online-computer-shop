package projects.onlineshop.domain.model.order;

import lombok.*;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.order.OrderAddress;
import projects.onlineshop.domain.model.order.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_orders")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    private List<Product> products;

    private BigDecimal price;

    private LocalDateTime orderDate;

    private Status status;

    @Embedded
    private OrderAddress orderAddress;

}
