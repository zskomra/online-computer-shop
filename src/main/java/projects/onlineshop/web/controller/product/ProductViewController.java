package projects.onlineshop.web.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.service.OrderService;
import projects.onlineshop.service.RatingService;
import projects.onlineshop.service.WatchProductService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product/view")
public class ProductViewController {

    private final ProductRepository productRepository;
    private final RatingService ratingService;
    private final OrderService orderService;
    private final WatchProductService watchProductService;


    @GetMapping("/{id}")
    private String prepareProductView(Model model,
                                      @PathVariable(name = "id") Long id) {

       Product product = productRepository.getOne(id);

       List<ProductRating> allComment = ratingService.findRatingByProductId(product);


       model.addAttribute("allRating", allComment);

       model.addAttribute("productId", product.getId());
       model.addAttribute("productName", product.getName());
       model.addAttribute("productDescription", product.getDescription());
       model.addAttribute("productPrice", product.getPrice());
       model.addAttribute("productRating", product.getProductRating());

       return "product/view";
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addProductToOrder(@RequestParam Long productId) {
        Long id = productId;
        log.debug("Pobrano id produktu: {}", id);
        orderService.addOrder(id);
        return "product/view";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
    public String saveProductToWatch(@RequestParam Long productId) {
        Long id = productId;
        log.debug("Pobrano id: {}", id);
        watchProductService.addToWatch(id);
        return "product/view";
    }


}
