package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.WatchProduct;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.domain.repository.WatchProductRepository;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class WatchProductService {


    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;
    private final WatchProductRepository watchProductRepository;


    @Transactional
    public boolean addToWatch (Long productId) {
        Long id = productId;
        User user = userService.getLoggedUser();
        log.debug("Pobrano uzytkownika: {}", user.getUsername());

        Product product = productService.getProductById(id);
        log.debug("Pobrano produkt: {}", product.getName());

        WatchProduct watchProduct = user.getWatchProduct();
        log.debug("Pobrano zapisane: {}", watchProduct);

        Set<Product> products = user.getWatchProduct().getProducts();
        log.debug("Pobrano listę produktów: {}" ,products.size());

        products.add(product);
        log.debug("Dodano produkt: {}", product.getName());

        log.debug("Produkty dodane do obserwowanych: {}", products.size());

        userRepository.save(user);
        log.debug("Zapisano w obserwowanych: {}", true);
        return true;
    }

    @Transactional
    public boolean confirmDeleteProduct(Long productId) {
        Long id = productId;
        User user = userService.getLoggedUser();
        log.debug("Pobrano uzytkownika: {}", user.getUsername());

        Product product = productService.getProductById(id);
        log.debug("Pobrano produkt: {}", product.getName());

        WatchProduct watchProduct = watchProductRepository.getOne(productId);;
        log.debug("Pobrano zapisane: {}", watchProduct);

        Set<Product> products = user.getWatchProduct().getProducts();
        log.debug("Pobrano listę produktów: {}" ,products.size());

        products.remove(product);

        userRepository.save(user);
        log.debug("Usunięto produkt z obserwowanych: {}", true);
        return true;
    }

}
