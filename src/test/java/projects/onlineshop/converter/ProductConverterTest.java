package projects.onlineshop.converter;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.web.command.CreateProductCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Product Specification")
class ProductConverterTest {

    ProductConverter cut;
    ProductCategoryConverter productCategoryConverter;
    ProductCategoryRepository categoryRepository;


    @BeforeEach
    void setUp(){
        categoryRepository = Mockito.mock(ProductCategoryRepository.class);
        productCategoryConverter = new ProductCategoryConverter();
        cut = new ProductConverter(categoryRepository, productCategoryConverter );
    }

    @Test
    @DisplayName("- should convert valid product command to product")
    void shouldConvertValidProductCommandToProduct() {

        ProductCategory productCategory = ProductCategory.builder()
                .id(1L)
                .name("Laptopy")
                .build();

        CreateProductCommand productCommand = DataHelper.createProductCommand("Super nowy czarno-różowy laptop","Acer",
                BigDecimal.valueOf(2000),1L);

        when(categoryRepository.getOne(1L)).thenReturn(productCategory);

        Product expected = DataHelper.product("Acer","Super nowy czarno-różowy laptop",productCategory,2000L);

        Product actual = cut.from(productCommand);

        assertEquals(expected,actual);
        assertEquals(expected.getPrice(),actual.getPrice());
        assertEquals(expected.getCategory().getId(),actual.getCategory().getId());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    @DisplayName("- should raise exception when converting from null")
    void shouldRaiseExceptionWhenConvertingFromNull() {
        CreateProductCommand command = null;

        assertThrows(IllegalArgumentException.class, () -> cut.from(command));
    }

}