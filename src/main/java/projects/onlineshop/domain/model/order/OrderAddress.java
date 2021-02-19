package projects.onlineshop.domain.model.order;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Data
public class OrderAddress {

    private String zipCode;
    private String town;
    private String street;
    private Integer homeNumber;
    private Integer flatNumber;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
}
