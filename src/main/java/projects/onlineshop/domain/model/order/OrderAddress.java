package projects.onlineshop.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
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
