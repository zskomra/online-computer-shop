package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.order.OrderAddress;
import projects.onlineshop.domain.model.order.UserOrder;
import projects.onlineshop.web.command.EditUserCommand;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserOrderConverter {

    public UserOrder from(EditUserCommand editUserCommand, Order order) {
        return UserOrder.builder()
                    .orderAddress(OrderAddress.builder()
                            .flatNumber(editUserCommand.getFlatNumber())
                            .homeNumber(editUserCommand.getHomeNumber())
                            .street(editUserCommand.getStreet())
                            .town(editUserCommand.getTown())
                            .userFirstName(editUserCommand.getFirstName())
                            .userLastName(editUserCommand.getLastName())
                            .zipCode(editUserCommand.getZipCode())
                            .build())
                    .products(new ArrayList<>(order.getProducts()))
                    .build();

    }

}
