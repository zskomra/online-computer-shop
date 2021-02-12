package projects.onlineshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.*;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.domain.repository.WatchProductRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Watch product service specification")
@ExtendWith(MockitoExtension.class)
class WatchProductServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    ProductService productService;
    @Mock
    WatchProductRepository watchProductRepository;

    @InjectMocks
    WatchProductService cut;

    @Nested
    @DisplayName("1. Add product to watch")
    class AddToWatch {

        @Test
        @DisplayName("- should add product to user watch products")
        void test1 (){
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            User user = DataHelper.user(2L, "user@user.pl", "password", new UserDetails(), new Order(), true, Set.of("ROLE_USER"),
                    WatchProduct.builder()
                    .products(new HashSet<>())
                    .build());

            when(userService.getLoggedUser()).thenReturn(user);
            when(productService.getProductById(3L)).thenReturn(product);

            boolean result = cut.addToWatch(3L);

            verify(userRepository,times(1)).save(user);
            assertTrue(result);
            assertEquals(1,user.getWatchProduct().getProducts().size());
        }

        @Test
        @DisplayName("- should add product to user watch products")
        void test2 (){
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            User user = DataHelper.user(2L, "user@user.pl", "password", new UserDetails(), new Order(), true, Set.of("ROLE_USER"),
                    WatchProduct.builder()
                            .products(new HashSet<>())
                            .build());
            user.getWatchProduct().getProducts().add(product);

            when(userService.getLoggedUser()).thenReturn(user);
            when(productService.getProductById(3L)).thenReturn(product);

            boolean result = cut.confirmDeleteProduct(3L);

            verify(userRepository,times(1)).save(user);
            assertTrue(result);
            assertEquals(0,user.getWatchProduct().getProducts().size());
        }

    }
}