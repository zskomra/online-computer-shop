package projects.onlineshop.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_rating")
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder @EqualsAndHashCode(of = "id")
@ToString(exclude = "user")
public class ProductRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    private String opinion;
    @Column(name = "date_comment")
    private String dateComment;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "username")
    private String username;

}