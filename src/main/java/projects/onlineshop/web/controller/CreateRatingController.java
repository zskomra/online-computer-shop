package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.service.RatingService;
import projects.onlineshop.web.command.CreateRatingCommand;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/product/rating")
@RequiredArgsConstructor
public class CreateRatingController {

    public final RatingService ratingService;

    @GetMapping
    public String newProductRating(Model model) {
        model.addAttribute("createRatingCommand", new CreateRatingCommand());
        return "product/rating";
    }

    @PostMapping
    public String addNewProductRating(@Valid CreateRatingCommand createRating, BindingResult bindingResult){
        log.debug("Dane oceny produktu: {}", createRating);
        if(bindingResult.hasErrors()) {
            log.debug("Bład danych: {}", bindingResult.getAllErrors());
            return "product/rating";
        }

        try {
            Long createId = ratingService.ratingCreate(createRating);
            log.debug("Utworzono opinię o id: {}",createId);
            return "redirect:/product/list";
        }
        catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd tworzenia oceny produktu", re);
            bindingResult.rejectValue(null, null,"Błąd oceny");
            return "product/rating";
        }

    }


}
