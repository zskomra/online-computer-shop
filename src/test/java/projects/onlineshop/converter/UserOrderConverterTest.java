package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.*;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.order.OrderAddress;
import projects.onlineshop.domain.model.order.UserOrder;
import projects.onlineshop.service.OrderService;
import projects.onlineshop.web.command.EditUserCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User order converter specification")
class UserOrderConverterTest {

    UserOrderConverter cut;

    @BeforeEach
    void setUp(){
        cut = new UserOrderConverter();
    }

    @DisplayName("1. Comverting from EditUserCommand and Order")
    @Nested
    class convertingFromEditUserCommandAndOrder {

        @Test
        @DisplayName("- should convert valid data to user order")
        void test1(){
            EditUserCommand command = DataHelper.editUserCommand("87-140","Chełmża","Bydgoska",
                    9,10,"Nowa","Jan" );
            User user = new User(1L,"user@user.pl","s3cr3t",true, Set.of("ROLE_USER"),new UserDetails(),new Order(),new WatchProduct(),new ArrayList<>());
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            Order order = Order.builder()
                    .id(4L)
                    .products(new ArrayList<>(List.of(product)))
                    .build();
            OrderAddress orderAddress = OrderAddress.builder().zipCode("87-140").userLastName("Nowa").userFirstName("Jan").town("Chełmża")
                    .street("Bydgoska").homeNumber(9).flatNumber(10).build();

            UserOrder expected = UserOrder.builder().orderAddress(orderAddress).products(new ArrayList<>(List.of(product))).build();

            UserOrder actual = cut.from(command, order);

            assertEquals(expected,actual);
        }
    }
}