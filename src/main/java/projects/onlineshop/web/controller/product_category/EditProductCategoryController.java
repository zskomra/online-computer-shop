package projects.onlineshop.web.controller.product_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projects.onlineshop.service.ProductCategoryService;
import projects.onlineshop.web.command.EditProductCategoryCommand;

import javax.validation.Valid;

@Controller @RequiredArgsConstructor
@Slf4j @RequestMapping("/product-category/edit")
public class EditProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping()
    public String prepareProductCategoryEditPage(Model model, Long productCategoryId) {
        EditProductCategoryCommand category = productCategoryService.findCategoryById(productCategoryId);

        if (category.getId().longValue() == productCategoryId.longValue()){
            model.addAttribute("editedCategory", category);
        }
        return "product-category/edit";
    }

    @PostMapping
    public String processEditProductCategory(@Valid EditProductCategoryCommand editedCategory,
                                             BindingResult bindingResult) {
        log.debug("Dane kategorii produktu do edycji: {}", editedCategory);
        if (bindingResult.hasErrors()) {
            log.debug("Błędne dane: {}", bindingResult.getAllErrors());
            return "product-category/edit";
        }

        try {
            productCategoryService.edit(editedCategory);
            log.debug("Edytowano kategorię produktu: {}", editedCategory);
            return "redirect:/product-category/list";
        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd podczas edycji katgorii produktu", re);
            bindingResult.rejectValue(null, null, "Wystąpił błąd");
            return "product-category/edit";
        }
    }
}
