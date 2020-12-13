package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.RatingConverter;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.RatingRepository;
import projects.onlineshop.exception.RatingAlreadyExistException;
import projects.onlineshop.web.command.CreateRatingCommand;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {


    private final RatingConverter ratingConverter;
    private final RatingRepository ratingRepository;

    @Transactional
    public Long ratingCreate(CreateRatingCommand rating) {
        log.debug("Dane do utworzenia oceny: {}", rating);


        if (ratingRepository.getIsDuplicateRating()) {
            log.debug("Komentarz dla danego produktu już istnieje");
            throw new RatingAlreadyExistException(
                    String.format("Ocena użytkownika o id: %s dla produktu o id: %s już istnieje",
                            rating.getUserId(),rating.getProductId())
            );
        }


        ProductRating ratingToSave = ratingConverter.from(rating);
        log.debug("Zapisano oocenę produktu: {}", ratingToSave);

        return ratingToSave.getId();
    }

}
