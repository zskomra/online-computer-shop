package projects.onlineshop.domain.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Builder @AllArgsConstructor @NoArgsConstructor
@Data @Table(name = "orders")
@ToString(exclude = {"user", "userId", "products"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Integer quantity;
//    private String orderDate;

    @OneToOne
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();



}
