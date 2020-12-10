package projects.onlineshop.converter;

import org.springframework.stereotype.Component;
import projects.onlineshop.data.ProductCategorySummary;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.web.command.CreateProductCategoryCommand;

@Component
public class ProductCategoryConverter {

    public ProductCategory from(CreateProductCategoryCommand createProductCategoryCommand) {
        return ProductCategory.builder()
                .name(createProductCategoryCommand.getName())
                .build();
    }

    public ProductCategorySummary toProductCategorySummary(ProductCategory category){
        return ProductCategorySummary.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}