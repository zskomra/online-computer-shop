package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.service.ProductService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller @Slf4j
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomePageController {

    private final ProductService productService;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @GetMapping
    public String getMainPage(Model model) {
        List<Product> getLast5Added = productService.getLast5AddedProductsSortedByDate();
        log.debug("Pobrano ostatnio dodane produktu: {}",getLast5Added.stream().map(Product::getName).collect(Collectors.toList()));

        Set<Product> getRecommended4Products = productService.getRecommend();
//        log.debug("Pobrano rekomendowane produkty: {}", getRecommended4Products.size());

        List<Product> productRatings = productService.top5ratedProducts();
//        log.debug("Pobrano najlepiej ocenianie produkty {}",productRatings.stream().map(Product::getName).collect(Collectors.toList()));

        model.addAttribute("last5Products",getLast5Added);
        model.addAttribute("recommended4Product", getRecommended4Products);
        model.addAttribute("top5Products",productRatings);
        return "home/main";
    }

    @GetMapping("/processors")
    public String getProcessors(Model model) {
        List<Product> products = loadProducts("Procesory");
        model.addAttribute("products",products);
        return "home/products";
    }

    @GetMapping("/monitors")
    public String getMonitors(Model model) {
        List<Product> products = loadProducts("Monitory");
        model.addAttribute("products",products);
        return "home/products";
    }

    @GetMapping("/keyboards")
    public String getKeyboards(Model model) {
        List<Product> products = loadProducts("Klawiatury");
        model.addAttribute("products",products);
        return "home/products";
    }

    @GetMapping("/mouses")
    public String getMouses(Model model) {
        List<Product> products = loadProducts("Myszki");
        model.addAttribute("products",products);
        return "home/products";
    }

    @GetMapping("/graphics-cards")
    public String getGraphicsCards(Model model) {
        List<Product> products = loadProducts("Karty graficzne");
        model.addAttribute("products",products);
        return "home/products";
    }

    @GetMapping("/power-supplies")
    public String getPowerSupplies(Model model) {
        List<Product> products = loadProducts("Zasilacze");
        model.addAttribute("products",products);
        return "home/products";
    }


    public List<Product> loadProducts(String category) {
        if(productCategoryRepository.existsByName(category)) {
            log.debug("Kategoria prodktu do pobrania: {}", category);
            ProductCategory productCategory = productCategoryRepository.getOneByName(category);
            log.debug("Pobrano kategorie : {}", productCategory.getName());
            List<Product> products = productService.getProductsByCategory(productCategory.getId());
            log.debug("Pobrano produktów : {}", products.size());
            return products;
        }
        return productRepository.findAll();
    }

}
