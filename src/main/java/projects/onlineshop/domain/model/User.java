package projects.onlineshop.domain.model;


import lombok.*;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.order.UserOrder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = "password")
@Builder
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;
    private String password;

    @Builder.Default
    private Boolean active = Boolean.FALSE;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetails userDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Order order;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WatchProduct watchProduct;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<UserOrder> userOrders;
}
