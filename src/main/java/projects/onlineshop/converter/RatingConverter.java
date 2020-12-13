package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.web.command.CreateRatingCommand;

@Component
@RequiredArgsConstructor
public class RatingConverter {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductRating from(CreateRatingCommand rating) {
        return ProductRating.builder()
                .rating(rating.getRating())
                .opinion(rating.getOpinion())
                .datePurchase(rating.getDatePurchase())
                .product(productRepository.getOne(rating.getProductId()))
                .user(userRepository.getOne(rating.getUserId()))
                .build();
    }
}
