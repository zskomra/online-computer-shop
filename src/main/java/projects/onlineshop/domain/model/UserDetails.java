package projects.onlineshop.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Table(name = "user_details")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
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
    @Column(name = "user_id")
    private Long userId;
}
