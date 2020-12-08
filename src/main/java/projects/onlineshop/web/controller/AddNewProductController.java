package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.converter.ProductCategoryConverter;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.service.ProductCategoryService;
import projects.onlineshop.service.ProductService;
import projects.onlineshop.web.command.CreateProductCategoryCommand;
import projects.onlineshop.web.command.CreateProductCommand;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product/add")
public class AddNewProductController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductCategoryConverter productCategoryConverter;
    private final ProductCategoryRepository productCategoryRepository;

    @ModelAttribute("productCategories")
    public List<CreateProductCategoryCommand> productCategories(){
        return productCategoryService.getAllCategories();
    }

    @GetMapping
    public String prepareAddProductPage(Model model) {
        model.addAttribute("createProductCommand", new CreateProductCommand());
        return "product/add";
    }

    @PostMapping
    public String processAddProduct(@Valid CreateProductCommand product,
                                    BindingResult bindingResult) {
        log.debug("Dane do utworzenie produktu: {}", product);
        if(bindingResult.hasErrors()){
            log.debug("Błędne dane: {}", bindingResult.getAllErrors());
            return "product/add";
        }

        try {
            productService.create(product);
            log.debug("Utworzono produkt: {}", product);
            return "product/list";
        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd podczas tworzenia produktu", re);
            bindingResult.rejectValue(null, null, "Wystąpił błąd");
            return "product/add";
        }
    }
}
