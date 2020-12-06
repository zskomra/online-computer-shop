package projects.onlineshop.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"password"})
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    private Boolean active = Boolean.FALSE;
}
