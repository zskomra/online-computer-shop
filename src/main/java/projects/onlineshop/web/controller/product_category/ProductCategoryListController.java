package projects.onlineshop.web.controller.product_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.data.ProductCategorySummary;
import projects.onlineshop.service.ProductCategoryService;

import java.util.List;

@Controller @Slf4j
@RequiredArgsConstructor
@RequestMapping("product-category/list")
public class ProductCategoryListController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    public String getProductCategoryList() {
        return "product-category/list";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategorySummary> loadProductCategories() {
        return productCategoryService.getAllCategories();
    }
}
