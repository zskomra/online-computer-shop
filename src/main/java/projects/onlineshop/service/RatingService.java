package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.RatingConverter;
import projects.onlineshop.data.CommentSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.repository.RatingRepository;
import projects.onlineshop.exception.RatingAlreadyExistException;
import projects.onlineshop.security.AuthenticatedUser;
import projects.onlineshop.web.command.CreateRatingCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {


    private final RatingConverter ratingConverter;
    private final RatingRepository ratingRepository;
    private final AuthenticatedUser authenticatedUser;


    @Transactional
    public Long ratingCreate(CreateRatingCommand createRating, Product product) {
        log.debug("Dane do utworzenia oceny: {}", createRating);

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String[] name = authenticatedUser.getUsername().split("@");
        String username = name[0];
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String commentDate = date.format(formatters);


        ProductRating ratingToSave = ratingConverter.from(createRating, product, username, commentDate);
        log.debug("Zapisano oocenę produktu: {}", ratingToSave);
        ratingRepository.save(ratingToSave);

        return ratingToSave.getId();
    }

    public List<ProductRating> findRatingByProductId(Product product) {
        if (product != null) {
            ProductRating exampleRating = new ProductRating();
            exampleRating.setProduct(product);
            Example<ProductRating> example = Example.of(exampleRating);


            return ratingRepository.findAll(example);
        }
        return null;
    }


}
