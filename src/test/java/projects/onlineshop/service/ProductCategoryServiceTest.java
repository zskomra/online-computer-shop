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
import projects.onlineshop.converter.ProductCategoryConverter;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.domain.repository.WatchProductRepository;
import projects.onlineshop.exception.ProductCategoryAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCategoryCommand;
import projects.onlineshop.web.command.EditProductCategoryCommand;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product category service specification")
class ProductCategoryServiceTest {
    @Mock
    ProductCategoryRepository productCategoryRepository;
    @Mock
    ProductCategoryConverter productCategoryConverter;
    @Captor
    ArgumentCaptor<ProductCategory> categoryCaptor;

    @InjectMocks
    ProductCategoryService cut;

    @Nested
    @DisplayName("1. Create category")
    class AddToWatch {

        @Test
        @DisplayName("- should create product category and set id with provided data")
        void shouldCreateCategoryAndSetIdWithProvidedData() {
            CreateProductCategoryCommand categoryCommand = CreateProductCategoryCommand.builder()
                    .name("Spaceship")
                    .build();
            ProductCategory productCategoryToCreate = ProductCategory.builder().name("Spaceship").build();

            when(productCategoryConverter.from(categoryCommand)).thenReturn(productCategoryToCreate);
            when(productCategoryRepository.save(categoryCaptor.capture())).thenAnswer(invocation -> {
                ProductCategory categoryToSave = invocation.getArgument(0);
                categoryToSave.setId(11L);
                return categoryToSave;
            });

            Long result = cut.create(categoryCommand);
            ProductCategory savedCategory = categoryCaptor.getValue();

            assertNotNull(savedCategory);
            assertEquals(11L, result);
            assertEquals("Spaceship", savedCategory.getName());

        }

        @DisplayName("- should raise error when category already exists")
        @Test
        void test1() {
            CreateProductCategoryCommand categoryCommand = CreateProductCategoryCommand.builder()
                    .name("Spaceship")
                    .build();

            when(productCategoryRepository.existsByName("Spaceship")).thenReturn(true);

            assertThatThrownBy(() -> cut.create(categoryCommand))
                    .isInstanceOf(ProductCategoryAlreadyExistsException.class)
                    .hasMessageContaining("Kategoria")
                    .hasNoCause();
            verifyNoMoreInteractions(productCategoryRepository);
            verifyNoInteractions(productCategoryConverter);
        }

    }

    @Nested
    @DisplayName("2. Edit category")
    class EditProductCategory {

        @Test
        @DisplayName("- should edit product category name with provided data")
        void test1() {
            EditProductCategoryCommand categoryCommand = EditProductCategoryCommand.builder()
                    .id(3L)
                    .name("Printer")
                    .build();
            ProductCategory productCategory = ProductCategory.builder()
                    .id(3L)
                    .name("Print device")
                    .build();

            when(productCategoryRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(productCategory));

            String result = cut.edit(categoryCommand);

            assertEquals("Printer", result);

        }

        @Test
        @DisplayName("- should raise an error when product category cannot be found")
        void test2() {
            EditProductCategoryCommand categoryCommand = EditProductCategoryCommand.builder()
                    .id(3L)
                    .name("Printer")
                    .build();

            assertThatThrownBy(() -> cut.edit(categoryCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNoCause();
        }
    }

    @DisplayName("3. Delete category")
    @Nested
    class DeleteCategory {

        @Test
        @DisplayName("- should delete product category when no products assigned")
        void test1() {
            ProductCategory productCategory = ProductCategory.builder()
                    .id(3L)
                    .name("Printer")
                    .products(Collections.emptySet())
                    .build();
            Optional<ProductCategory> optionalProductCategory = Optional.of(productCategory);
            when(productCategoryRepository.findById(3L)).thenReturn(optionalProductCategory);

            boolean result = cut.delete(3L);

            verify(productCategoryRepository, times(1)).deleteById(3L);
            assertTrue(result);
        }

        @Test
        @DisplayName("- should not delete category when products are assigned")
        void test2() {
            ProductCategory productCategory = ProductCategory.builder()
                    .id(3L)
                    .name("Printer")
                    .products(Set.of(Product.builder().build()))
                    .build();
            Optional<ProductCategory> optionalProductCategory = Optional.of(productCategory);
            when(productCategoryRepository.findById(3L)).thenReturn(optionalProductCategory);

            boolean result = cut.delete(3L);

            assertFalse(result);
        }
    }
}



