package projects.helpers;

import projects.onlineshop.domain.model.*;
import projects.onlineshop.web.command.*;

import java.math.BigDecimal;
import java.util.Set;

public class DataHelper {

    public static RegisterUserCommand registerUserCommand(String username, String password) {
        RegisterUserCommand command = new RegisterUserCommand();
        command.setUsername(username);
        command.setPassword(password);
        return command;
    }

    public static User user(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static User user(Long id, String username, String password, UserDetails userDetails, Order order, Boolean active, Set<String> roles, WatchProduct watchProduct ) {
        return User.builder()
                .id(id)
                .order(order)
                .active(active)
                .roles(roles)
                .watchProduct(watchProduct)
                .username(username)
                .password(password)
                .userDetails(userDetails)
                .build();
    }

    public static EditUserCommand editUserCommand(String zipcode, String town, String street,Integer homeNumber,
                                                  Integer flatNumber, String lastName, String firstName) {
        return EditUserCommand.builder()
                .zipCode(zipcode)
                .town(town)
                .street(street)
                .homeNumber(homeNumber)
                .flatNumber(flatNumber)
                .lastName(lastName)
                .firstName(firstName)
                .build();
    }

    public static UserDetails userDetails (String zipcode, String town, String street,Integer homeNumber,
                                           Integer flatNumber, String lastName, String firstName) {
        return UserDetails.builder()
                .zipCode(zipcode)
                .town(town)
                .street(street)
                .homeNumber(homeNumber)
                .flatNumber(flatNumber)
                .lastName(lastName)
                .firstName(firstName)
                .build();
    }

    public static Contact contact (String topic, String description, String email, String contentForAdmin) {
        return Contact.builder()
                .topic(topic)
                .description(description)
                .email(email)
                .contentForAdmin(contentForAdmin)
                .build();
    }

    public static ContactCommand contactCommand (String topic, String description, String email) {
        return ContactCommand.builder()
                .topic(topic)
                .description(description)
                .email(email)
                .build();
    }

    public static CreateRatingCommand ratingCommand (Integer rating,  String opinion, String title) {
        return CreateRatingCommand.builder()
                .rating(rating)
                .opinion(opinion)
                .title(title)
                .build();
    }

    public static Product product(Long id,String name, String description, ProductCategory productCategory, Long price) {
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .category(productCategory)
                .price(BigDecimal.valueOf(price))
                .build();
    }

    public static ProductRating productRating(Integer rating, String title, String opinion, String dateComment, Product product, String username) {
        return ProductRating.builder()
                .rating(rating)
                .title(title)
                .opinion(opinion)
                .dateComment(dateComment)
                .product(product)
                .username(username)
                .build();
    }

    public static CreateProductCommand createProductCommand (String description, String name, BigDecimal price, Long productCategoryId ) {
        return CreateProductCommand.builder()
                .description(description)
                .name(name)
                .price(price)
                .categoryId(productCategoryId)
                .build();
    }
}
