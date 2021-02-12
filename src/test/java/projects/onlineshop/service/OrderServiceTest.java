package projects.onlineshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.*;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.repository.OrderRepository;
import projects.onlineshop.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Order Service Specification")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    OrderService cut;


    @DisplayName("1. Add product to order")
    @Nested
    class AddOrder {

        @Test
        @DisplayName("- should add expected product to logged user order")
        void shouldAddExpectedProductToLoggedUserOrder() {
            User user = DataHelper.user(2L, "user@user.pl", "password", new UserDetails(), new Order(), true, Set.of("ROLE_USER"), new WatchProduct());
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            Product product2 = DataHelper.product(4L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);

            when(userService.getLoggedUser()).thenReturn(user);
            when(productService.getProductById(3L)).thenReturn(product);
            when(productService.getProductById(4L)).thenReturn(product2);

            boolean expectedToAddFirstProduct = cut.addOrder(3L);
            boolean expectedToAddSecondProduct = cut.addOrder(4L);

            assertTrue(expectedToAddFirstProduct);
            assertEquals(2, user.getOrder().getProducts().size());

        }

        @Test
        @DisplayName("- should raise an exception when user is not logged")
        void shouldRaiseExceptionWhenUserIsNotLogged() {

            assertThatThrownBy(() -> cut.addOrder(3L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("logged user")
                    .hasNoCause();
        }

    }

    @DisplayName("2. Confirm ordered products")
    @Nested
    class ConfirmOrder {

        @Test
        @DisplayName("- should clear user order list")
        void shouldClearUserOrderList(){
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            Order order = Order.builder()
                    .id(3L)
                    .products(new ArrayList<>())
                    .build();
            order.getProducts().add(product);

            when(orderRepository.getOne(3L)).thenReturn(order);

            boolean confirmOrder = cut.confirmOrder(3L);

            assertTrue(confirmOrder);
            assertEquals(0,order.getProducts().size());

        }

    }

    @Nested
    @DisplayName("3. Confirm Delete Product from Cart")
    class DeleteProductFromCart {

        @Test
        @DisplayName("- should remove product from cart")
        void test1() {
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            Order order = Order.builder()
                    .id(4L)
                    .products(new ArrayList<>(List.of(product)))
                    .build();
            User user = DataHelper.user("user@user.pl","password");
            user.setOrder(order);

            when(userService.getLoggedUser()).thenReturn(user);
            when(productService.getProductById(3L)).thenReturn(product);
            when(orderRepository.getByUserUsername("user@user.pl")).thenReturn(order);

            boolean result = cut.deleteProductFromCart(3L);

            assertTrue(result);
            assertEquals(0,order.getProducts().size());
        }
        @Test
        @DisplayName("- should do nothing when cart do not contain product")
        void test2(){
            Product product = DataHelper.product(3L, "Mouse", "Crazy new mouse", new ProductCategory(), 200L);
            Product product2 = DataHelper.product(5L, "Cat", "Spooky new cat", new ProductCategory(), 100L);
            Order order = Order.builder()
                    .id(4L)
                    .products(new ArrayList<>(List.of(product2)))
                    .build();
            User user = DataHelper.user("user@user.pl","password");
            user.setOrder(order);

            when(userService.getLoggedUser()).thenReturn(user);
            when(productService.getProductById(3L)).thenReturn(product);
            when(orderRepository.getByUserUsername("user@user.pl")).thenReturn(order);

            boolean result = cut.deleteProductFromCart(3L);

            assertFalse(result);
            assertEquals(1,order.getProducts().size());
        }
    }
}