package projects.onlineshop.web.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.data.CommentSummary;
import projects.onlineshop.domain.model.Order;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.service.OrderService;
import projects.onlineshop.service.RatingService;
import projects.onlineshop.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product/view")
public class ProductViewController {

    private final ProductRepository productRepository;
    private final RatingService ratingService;
    private final OrderService orderService;
    private final UserService userService;


    @GetMapping("/{id}")
    private String prepareProductView(Model model,
                                      @PathVariable(name = "id") Long id) {

       Product product = productRepository.getOne(id);

       List<ProductRating> allComment = ratingService.findRatingByProductId(product);


       model.addAttribute("allRating", allComment);
       model.addAttribute("tableSize", allComment.size());

       model.addAttribute("productId", product.getId());
       model.addAttribute("productName", product.getName());
       model.addAttribute("productDescription", product.getDescription());
       model.addAttribute("productPrice", product.getPrice());
       model.addAttribute("productRating", product.getProductRating());

       return "product/view";
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addProductToOrder(@RequestParam Long productId) {
        log.debug("Pobrano id produktu: {}", productId);
        orderService.addOrder(productId);
        return "redirect:/product/view/" + productId;
    }


}
