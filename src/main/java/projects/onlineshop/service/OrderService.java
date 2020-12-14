package projects.onlineshop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.domain.model.Order;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.repository.OrderRepository;
import projects.onlineshop.domain.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public List<Product> getUserOrders(User loggedUser) {
        return userRepository.getUsersByUsername(loggedUser.getUsername()).getOrder().getProducts();
    }

    @Transactional
    public boolean addOrder (Long productId) {
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
        for (int i = 0; i < userOrders.size(); i++) {
            BigDecimal currentPrice = userOrders.get(i).getPrice();
            bigDecimal = bigDecimal.add(currentPrice);
        }
        return bigDecimal;
    }
    @Transactional
    public boolean buyOrder(Long orderId) {
        Order order =orderRepository.getOne(orderId);
        log.debug("Zamowien w koszyku: {}",order.getProducts().size());
        order.getProducts().clear();
        log.debug("Zamowien w koszyku: {}",order.getProducts().size());
        orderRepository.save(order);
        return true;
    }
}
