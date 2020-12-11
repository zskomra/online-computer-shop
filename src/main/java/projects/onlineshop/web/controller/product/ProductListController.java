package projects.onlineshop.web.controller.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.converter.ProductConverter;
import projects.onlineshop.data.ProductSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.service.ProductService;

import java.util.List;

@Controller @Slf4j @AllArgsConstructor
@RequestMapping("product/list")
public class ProductListController {

    private final ProductService productService;
    private final ProductConverter productConverter;

    @GetMapping
    public String prepareProductListHomePage(Model model) {
        return prepareProductListPage(model, 1);
    }

    @GetMapping("/page/{pageNum}")
    public String prepareProductListPage(Model model,
                                         @PathVariable(name = "pageNum") int pageNum) {
        Page<Product> page = productService.getAllProducts(pageNum);

        List<ProductSummary> allProductSummaries = productService.mapProductsToProductsSummaries(page);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("allProducts", allProductSummaries);

        return "product/list";
    }
}