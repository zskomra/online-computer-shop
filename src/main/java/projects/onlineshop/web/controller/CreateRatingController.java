package projects.onlineshop.web.controller;

import com.mysql.cj.xdevapi.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.service.RatingService;
import projects.onlineshop.web.command.CreateRatingCommand;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/product/rating")
@RequiredArgsConstructor
public class CreateRatingController {

    public final RatingService ratingService;
    private final ProductRepository productRepository;

    @GetMapping("/{id}")
    public String newProductRating(Model model,
                                   @PathVariable(name = "id") Long id) {
        Product product = productRepository.getOne(id);
        model.addAttribute("createRatingCommand", new CreateRatingCommand());
        model.addAttribute("productName", product.getName());
        model.addAttribute("productId",product.getId());
        return "product/rating";
    }

    @PostMapping
    public String addNewProductRating(@Valid CreateRatingCommand createRating,
                                      BindingResult bindingResult){

        Product product = productRepository.getOne(createRating.getCurrentProductId());
        String number = product.getId().toString();

        log.debug("Dane oceny produktu: {}", createRating);
        if(bindingResult.hasErrors()) {
            log.debug("Bład danych: {}", bindingResult.getAllErrors());
            return "product/rating";
        }

        try {
            ratingService.ratingCreate(createRating, product);
            log.error("Utworzono opinię o id");
            return "redirect:/product/view/" + number;
        }
        catch (RuntimeException re) {
            log.error(re.getLocalizedMessage());
            log.error("Błąd tworzenia oceny produktu", re);
            bindingResult.rejectValue(null, null,"Błąd oceny");
            return "redirect:/product/list";
        }
    }
}
