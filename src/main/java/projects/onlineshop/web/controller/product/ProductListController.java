package projects.onlineshop.web.controller.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.data.ProductSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.service.ProductService;

import java.util.List;

@Controller @Slf4j @AllArgsConstructor
@RequestMapping("product/list")
public class ProductListController {

    private final ProductService productService;

    @GetMapping
    public String prepareProductListHomePage(Model model,
                                             @RequestParam(value = "searchRegex",
                                                     required = false,
                                                     defaultValue = "") String searchRegex) {
        String trim = searchRegex.trim();
        log.debug("model: {}", model);
        return "redirect:/product/list/filter/page/1?sortField=name&sortDir=asc&searchRegex=" + trim;
    }

    @GetMapping("filter/page/{pageNum}")
    public String prepareProductFilteredListPage(Model model,
                                                 @PathVariable(name = "pageNum") int pageNum,
                                                 @RequestParam("sortField") String sortField,
                                                 @RequestParam("sortDir") String sortDir,
                                                 @RequestParam(value = "searchRegex", required = false) String searchRegex) {

        Page<Product> page;
        if (searchRegex != null) {
            page = productService.getAllProductsFiltered(pageNum, sortField, sortDir, searchRegex);
        } else {
            page = productService.getAllProducts(pageNum, sortField, sortDir);
        }

        List<ProductSummary> allProductSummaries = productService.mapProductsToProductsSummaries(page);

        addPageAndSortParamsToModel(model, pageNum, sortField, sortDir, searchRegex, page, allProductSummaries);

        return "product/list";
    }

    private void addPageAndSortParamsToModel(Model model,
                                             int pageNum,
                                             String sortField,
                                             String sortDir,
                                             String searchRegex,
                                             Page<Product> page,
                                             List<ProductSummary> allProductSummaries) {
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("searchRegex", searchRegex);

        model.addAttribute("allProducts", allProductSummaries);
    }
}
