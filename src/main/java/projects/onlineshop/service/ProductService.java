package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ProductConverter;
import projects.onlineshop.data.ProductSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.exception.ProductAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCommand;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Transactional
    public Long create(CreateProductCommand product) {
        log.debug("Dane do utworzenia produktu: {}", product);

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

    public Page<Product> getAllProducts(int pageNum) {
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return productRepository.findAll(pageable);
    }

    public List<ProductSummary> mapProductsToProductsSummaries(Page<Product> allProducts) {
        return allProducts.stream()
                .map(productConverter::fromProductToProductSummary)
                .collect(Collectors.toList());
    }
}
