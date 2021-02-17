package projects.onlineshop.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@Table(name="watch_product")
@ToString(exclude = {"user", "userId", "products"})
@Builder
public class WatchProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}
