package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.data.ProductCategorySummary;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.web.command.CreateProductCategoryCommand;
import projects.onlineshop.web.command.CreateProductCommand;
import projects.onlineshop.web.command.EditProductCategoryCommand;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Product Category Specification")
class ProductCategoryConverterTest {

    ProductCategoryConverter cut;
    ProductCategorySummary categorySummary;

    @BeforeEach
    void setUp() {
        cut = new ProductCategoryConverter();
        categorySummary = new ProductCategorySummary();
    }

    @Test
    @DisplayName("- should convert valid creation of category to product category")
    void shouldConvertValidCreationOfCategoryToProductCategory() {
        CreateProductCategoryCommand categoryCommand = CreateProductCategoryCommand.builder()
                .name("Laptopy")
                .build();

        ProductCategory expected = ProductCategory.builder().name("Laptopy").build();
        ProductCategory actual = cut.from(categoryCommand);

        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("- should convert valid product category to product category summmary")
    void shouldConvertValidCategoryToProductCategorySummary(){
        ProductCategory productCategory = ProductCategory.builder()
                .id(2L)
                .name("Laptopy")
                .build();

        ProductCategorySummary expected = ProductCategorySummary.builder()
                .id(2L)
                .name("Laptopy")
                .build();

        ProductCategorySummary actual = cut.toProductCategorySummary(productCategory);

        assertEquals(expected,actual);

    }

    @Test
    @DisplayName("-should prepare product category to edit ")
    void shouldPrepareProductCategoryToEdit() {
        ProductCategory actual = ProductCategory.builder()
                .id(1L)
                .name("Laptopy")
                .build();

        EditProductCategoryCommand expected = EditProductCategoryCommand.builder()
                .id(1L)
                .name("Laptopy")
                .build();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());

    }

    @Test
    @DisplayName("- should raise exception when converting from null")
    void shouldRaiseExceptionWhenConvertingFromNull() {
        CreateProductCategoryCommand command = null;

        assertThrows(IllegalArgumentException.class, () -> cut.from(command));
    }
}