package projects.onlineshop.domain.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import java.util.Set;


@Entity
@Table(name = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "name")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Length(max = 600)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private ProductCategory category;

    private BigDecimal price;


    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "rating_id")
    private Set<ProductRating> productRating;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private Set<WatchProduct> watchProducts = new HashSet<>();

}
