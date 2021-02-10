package projects.onlineshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projects.helpers.DataHelper;
import projects.onlineshop.converter.ProductConverter;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.domain.repository.RatingRepository;
import projects.onlineshop.exception.ProductAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCommand;

import javax.xml.crypto.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product service specification")
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductConverter productConverter;
    @Mock
    RatingRepository ratingRepository;
    @Mock
    ProductCategoryRepository productCategoryRepository;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @InjectMocks
    ProductService cut;

    @DisplayName("1. Create product")
    @Nested
    class CreateProduct {

        @Test
        @DisplayName("- should save product with provided data")
        void test1() {
            ProductCategory productCategory = ProductCategory.builder().id(2L).build();
            CreateProductCommand createProductCommand = DataHelper.createProductCommand("Brand new super fast keyboard","Keyboard", BigDecimal.valueOf(200L),2L);
            Product productToCreate = Product.builder()
                    .description("Brand new super fast keyboard")
                    .name("Keyboard")
                    .price(BigDecimal.valueOf(200L))
                    .category(productCategory)
                    .build();

            when(productConverter.from(createProductCommand)).thenReturn(productToCreate);
            when(productRepository.save(productCaptor.capture())).thenAnswer(invocation -> {
                Product productToSave = invocation.getArgument(0);
                productToSave.setId(5L);

                return productToSave;
            });

            Long result = cut.create(createProductCommand);
            Product savedProduct = productCaptor.getValue();

            assertNotNull(savedProduct);
            assertEquals(5L, result);
            assertThat(savedProduct.getAddDate())
                    .isInstanceOf(LocalDate.class)
                    .isNotNull();

        }

        @Test
        @DisplayName("- should")
        void tes2(){
            CreateProductCommand createProductCommand = CreateProductCommand.builder().name("product").build();
            when(productRepository.existsByName("product")).thenReturn(true);

            assertThatThrownBy(() ->cut.create(createProductCommand))
                    .isInstanceOf(ProductAlreadyExistsException.class)
                    .hasNoCause()
                    .hasMessageContaining("Produkt");
           verifyNoInteractions(productConverter);
           verifyNoMoreInteractions(productRepository);
        }

    }

}