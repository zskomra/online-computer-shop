package projects.onlineshop.web.controller.product_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.service.ProductCategoryService;

@Controller @Slf4j
@RequiredArgsConstructor @RequestMapping("/product-category/delete")
public class DeleteProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    public String processDeleteProductCategory(Long productCategoryId) {

        log.debug("Id kategorii produktu do usunięcia: {}", productCategoryId);
        productCategoryService.delete(productCategoryId);

        return "redirect:/product-category/list";
    }
}
