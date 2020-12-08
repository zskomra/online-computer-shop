package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.web.command.CreateProductCommand;

@Component @RequiredArgsConstructor
public class ProductConverter {

    private final ProductCategoryConverter productCategoryConverter;

    public Product from (CreateProductCommand command) {
        return Product.builder()
                .name(command.getName())
                .description(command.getDescription())
                .category(productCategoryConverter.from(command.getCategory()))
                .price(command.getPrice())
                .build();
    }
}
