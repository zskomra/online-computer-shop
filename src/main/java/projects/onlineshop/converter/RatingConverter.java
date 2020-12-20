package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.data.CommentSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.web.command.CreateRatingCommand;

@Component
@RequiredArgsConstructor
public class RatingConverter {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CommentSummary fromRatingToCommentSummary(ProductRating productRating) {
        return CommentSummary.builder()
                .id(productRating.getId())
                .rating(productRating.getRating())
                .opinion(productRating.getOpinion())
                .dateComment(productRating.getDateComment())
                .username(productRating.getUsername())
                .product(productRating.getProduct())
                .build();
    }

    public ProductRating from(CreateRatingCommand createRating, Product product, String username, String date) {
        return ProductRating.builder()
                .id(product.getId())
                .rating(createRating.getRating())
                .title(createRating.getTitle())
                .opinion(createRating.getOpinion())
                .dateComment(date)
                .product(product)
                .username(username)
                .build();
    }
}
