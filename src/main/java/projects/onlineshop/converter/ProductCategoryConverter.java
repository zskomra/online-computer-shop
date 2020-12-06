package projects.onlineshop.converter;

import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.web.command.CreateProductCategoryCommand;

@Component
public class ProductCategoryConverter {

    public ProductCategory from(CreateProductCategoryCommand createProductCategoryCommand) {
        return ProductCategory.builder()
                .name(createProductCategoryCommand.getName())
                .build();
    }
}