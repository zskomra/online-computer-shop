package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ProductCategoryConverter;
import projects.onlineshop.data.ProductCategorySummary;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.exception.ProductCategoryAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCategoryCommand;
import projects.onlineshop.web.command.EditProductCategoryCommand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @Slf4j @RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryConverter productCategoryConverter;

    @Transactional
    public Long create(CreateProductCategoryCommand category) {
        log.debug("Dane do utworzenia kategorii produktu: {}", category);

        if (productCategoryRepository.existsByName(category.getName())) {
            log.debug("Próba dodania kategorii o istniejącej nazwie");
            throw new ProductCategoryAlreadyExistsException(
                    String.format("Kategoria produktu o nazwie %s już istnieje", category.getName())
            );
        }

        ProductCategory productCategory = productCategoryConverter.from(category);
        productCategoryRepository.save(productCategory);

        log.debug("Zapisano kategorię produktu: {}", productCategory);

        return productCategory.getId();
    }

    @Transactional
    public List<ProductCategorySummary> getAllCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map((x) -> productCategoryConverter.toProductCategorySummary(x))
                .collect(Collectors.toList());
    }

    public EditProductCategoryCommand findCategoryById(Long categoryId) {
        ProductCategory category = productCategoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
        return productCategoryConverter.fromProductCategoryToEditProductCategoryCommand(category);
    }

    @Transactional
    public void edit(EditProductCategoryCommand editedCategory) {
        ProductCategory productCategory = productCategoryRepository.findById(editedCategory.getId()).orElseThrow(IllegalArgumentException::new);
        productCategory.setName(editedCategory.getName());
    }

    public void delete(Long productCategoryId) {
        ProductCategory category = productCategoryRepository.findById(productCategoryId).orElseThrow(IllegalArgumentException::new);
        if (category != null) {
            if (category.getProducts().isEmpty()) {
                productCategoryRepository.deleteById(productCategoryId);
                log.debug("Usunięto kategorię: {}", category);
            } else {
                log.debug("Nie można usunąć kategorii, ponieważ ma przypisane produkty!");
            }
        }
    }
}
