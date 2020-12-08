package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.service.ProductCategoryService;
import projects.onlineshop.web.command.CreateProductCategoryCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/product-category/add")
@RequiredArgsConstructor
@Slf4j
public class AddNewProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    public String prepareAddProductCategoryPage(Model model) {
        model.addAttribute("createProductCategoryCommand", new CreateProductCategoryCommand());
        return "product-category/add";
    }

    @PostMapping
    public String processAddProductCategory(@Valid CreateProductCategoryCommand category,
                                            BindingResult bindingResult) {
        log.debug("Dane do utworzenia kategorii produktu: {}", category);
        if (bindingResult.hasErrors()) {
            log.debug("Błędne dane: {}", bindingResult.getAllErrors());
            return "product-category/add";
        }

        try {
            productCategoryService.create(category);
            log.debug("Utworzono kategorię produktu: {}", category);
            return "product-category/list";
        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd podczas tworzenia kategorii produktu", re);
            bindingResult.rejectValue(null, null, "Wystąpił błąd");
            return "product-category/add";
        }
    }
}
