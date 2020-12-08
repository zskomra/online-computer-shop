package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ProductCategoryConverter;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.exception.ProductCategoryAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCategoryCommand;

@Service @Slf4j @RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryConverter productCategoryConverter;

    @Transactional
    public void create(CreateProductCategoryCommand category) {
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
    }
}
