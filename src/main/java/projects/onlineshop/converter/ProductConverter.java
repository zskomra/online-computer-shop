package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.web.command.CreateProductCommand;

@Component @RequiredArgsConstructor
public class ProductConverter {

    private final ProductCategoryRepository productCategoryRepository;

    public Product from (CreateProductCommand command) {
        return Product.builder()
                .name(command.getName())
                .description(command.getDescription())
                .category(productCategoryRepository.getOne(command.getCategoryId()))
                .price(command.getPrice())
                .build();
    }
}
