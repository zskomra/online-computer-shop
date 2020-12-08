package projects.onlineshop.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ProductConverter;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.exception.ProductAlreadyExistsException;
import projects.onlineshop.exception.ProductCategoryAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCommand;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Transactional
    public Long create(CreateProductCommand product) {
        log.debug("Dane do utworzenia kategorii produktu: {}", product);

        if(productRepository.existsByName(product.getName())) {
            log.debug("Produkt o podanej nazwie istnieje w bazie");
            throw new ProductAlreadyExistsException(
                    String.format("Produkt o nazwie %s już istnieje", product.getName()));
        }

        Product productToSave = productConverter.from(product);
        productRepository.save(productToSave);
        log.debug("Zapisano produkt: {}", productToSave);

        return productToSave.getId();
    }
}
