package projects.onlineshop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.UserOrderConverter;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.order.Status;
import projects.onlineshop.domain.model.order.UserOrder;
import projects.onlineshop.domain.repository.OrderRepository;
import projects.onlineshop.domain.repository.UserOrderRepository;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.web.command.EditUserCommand;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderRepository userOrderRepository;

    public List<Product> getUserOrders(User loggedUser) {
        return userRepository.getUsersByUsername(loggedUser.getUsername()).getOrder().getProducts();
    }

    @Transactional
    public boolean addOrder (Long productId) {
        if(userService.getLoggedUser() == null) throw new IllegalStateException("Option for logged users");
            User user = userService.getLoggedUser();
            log.debug("Pobrano uzytkownika: {}", user.getUsername());
            Product product = productService.getProductById(productId);
            log.debug("Pobrano produkt: {}", product.getName());
            Order order = user.getOrder();
            log.debug("Pobrano koszyk: {}", order);
            List<Product> products = user.getOrder().getProducts();
            log.debug("Pobrano produkty: {}" ,products.size());
            products.add(product);
            log.debug("Dodano produkt: {}", product.getName());
            log.debug("Produkty w koszyku: {}", products.size());
            userRepository.save(user);
            log.debug("Zapisano koszyk: {}", true);
                return true;
    }


    public BigDecimal getOrderSum() {
        List<Product> userOrders = getUserOrders(userService.getLoggedUser());
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (Product userOrder : userOrders) {
            BigDecimal currentPrice = userOrder.getPrice();
            bigDecimal = bigDecimal.add(currentPrice);
        }
        return bigDecimal;
    }
    @Transactional
    public Long confirmOrder(Long orderId, EditUserCommand editUserCommand) {
        Order cart = orderRepository.getOne(orderId);
        if (cart.getProducts().size() > 0) {
            UserOrder userOrder = userOrderConverter.from(editUserCommand, cart);
            userOrder.setOrderDate(LocalDate.now());
            userOrder.setPrice(getOrderSum());
            userOrder.setStatus(Status.IN_PROGRESS);
            userOrder.setUser(userService.getLoggedUser());
            userOrderRepository.save(userOrder);

            log.debug("Zamowione produkty: {}", userOrder.getProducts().size());
            cart.getProducts().clear();
            log.debug("Zamowien w koszyku: {}", cart.getProducts().size());
            orderRepository.save(cart);
            return userOrder.getId();

        }return null;
    }

    //todo do usuniecia zostawione na potrzebe testow
    @Transactional
    public boolean confirmOrder(Long orderId) {
        Order order =orderRepository.getOne(orderId);
        log.debug("Zamowien w koszyku: {}",order.getProducts().size());
        order.getProducts().clear();
        log.debug("Zamowien w koszyku: {}",order.getProducts().size());
        orderRepository.save(order);
        return true;
    }

    public boolean deleteProductFromCart(Long productId) {
        User user = userService.getLoggedUser();
        Order userCart = orderRepository.getByUserUsername(user.getUsername());
        Product productById = productService.getProductById(productId);
        List<Product> products = userCart.getProducts();
        if(!products.contains(productById))
            return false;
        products.remove(productById);
        orderRepository.save(userCart);
        return true;
    }
}
