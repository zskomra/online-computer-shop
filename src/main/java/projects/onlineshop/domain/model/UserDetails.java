package projects.onlineshop.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "user_details")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"user", "userId"})
public class UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String town;
    private String street;
    private Integer homeNumber;
    private Integer flatNumber;

    @OneToOne
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
}
