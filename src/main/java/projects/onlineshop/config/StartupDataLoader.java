package projects.onlineshop.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.domain.model.*;
import projects.onlineshop.domain.repository.*;

import java.math.BigDecimal;
import java.util.Set;

@Component @Slf4j @RequiredArgsConstructor
public class StartupDataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final OrderRepository orderRepository;

    @EventListener
    public void onStartupPrepareData(ContextRefreshedEvent event) {
        log.info("Loading startup data");

        loadUsersData();
        loadUserDetailsData();
        loadProductCategoryData();
        loadProductData();

        log.info("Startup data loaded");
    }

    private void loadProductData() {
        productRepository.save(Product.builder()
                .name("Intel Core i7-10700KF")
                .description("Poznaj moc do dziesiątej potęgi. Nowy, odblokowany procesor " +
                        "Intel® Core™ i7-10700KF z rodziny Comet Lake zapewnia znacznie wyższą " +
                        "wydajność.")
                .category(productCategoryRepository.getOneByName("Procesory"))
                .price(BigDecimal.valueOf(1669))
                .build());

        productRepository.save(Product.builder()
                .name("Intel Core i3-10100F")
                .description("Poznaj moc do dziesiątej potęgi. Nowy procesor Intel® Core™ i3-10100F " +
                        "z rodziny Comet Lake zapewnia wysoką wydajność, która przekłada się na " +
                        "wzrost produktywności i fantastyczną rozrywkę.")
                .category(productCategoryRepository.getOneByName("Procesory"))
                .price(BigDecimal.valueOf(419))
                .build());

        productRepository.save(Product.builder()
                .name("Intel Core i9-10850K")
                .description("Wbudowane inteligentne funkcje wydajności uczą się i przystosowują do " +
                        "nawyków użytkownika oraz dynamicznie kierują moc, tam gdzie jest najbardziej " +
                        "potrzebna.")
                .category(productCategoryRepository.getOneByName("Procesory"))
                .price(BigDecimal.valueOf(1669))
                .build());

        productRepository.save(Product.builder()
                .name("LG 29WL50S-B HDR10")
                .description("Uniwersalne zastosowanie i energooszczędność sprawiają, " +
                        "że monitor LG 29WL50S-B to doskonały wybór dla osób spędzających wiele godzin przed ekranem." +
                        " Wysoką rozdzielczość docenią miłośnicy filmów i seriali, natomiast duża " +
                        "przestrzeń robocza przypadnie do gustu osobom pracującym z danymi w arkuszach " +
                        "kalkulacyjnych.")
                .category(productCategoryRepository.getOneByName("Monitory"))
                .price(BigDecimal.valueOf(999))
                .build());

        productRepository.save(Product.builder()
                .name("Acer EK240YABI czarny")
                .description("Wykorzystaj w pełni swój wyświetlacz, gdy kolory na ekranie ożyją w " +
                        "rozdzielczości Full HD 1920 x 1080. Zobacz każdy szczegół i ciesz się " +
                        "klarownością obrazu grając w gry, oglądając filmy czy przeglądając internet. " +
                        "Podziel się tym, co jest na ekranie z rodziną i przyjaciółmi, " +
                        "ponieważ kolory pozostają wierne pod kątem widzenia do 178 stopni.")
                .category(productCategoryRepository.getOneByName("Monitory"))
                .price(BigDecimal.valueOf(999))
                .build());

        productRepository.save(Product.builder()
                .name("Microsoft All-in-One Media Keyboard")
                .description("Klawiatura All-in-One Media Keyboard jest idealnym urządzeniem do " +
                        "salonu lub domowego biura. Jest wyposażona w pełnowymiarowy zestaw klawiszy oraz " +
                        "zintegrowany, wielodotykowy trackpad. Dzięki temu pisanie, przesuwanie, przeciąganie, " +
                        "powiększanie i klikanie jest bardzo łatwe. ")
                .category(productCategoryRepository.getOneByName("Klawiatury"))
                .price(BigDecimal.valueOf(168.99))
                .build());

        productRepository.save(Product.builder()
                .name("Apple Magic Keyboard z Polem Numerycznym White")
                .description("Klawiatura All-in-One Media Keyboard jest idealnym urządzeniem do " +
                        "salonu lub domowego biura. Jest wyposażona w pełnowymiarowy zestaw klawiszy oraz " +
                        "zintegrowany, wielodotykowy trackpad. Dzięki temu pisanie, przesuwanie, przeciąganie, " +
                        "powiększanie i klikanie jest bardzo łatwe. ")
                .category(productCategoryRepository.getOneByName("Klawiatury"))
                .price(BigDecimal.valueOf(539))
                .build());

        productRepository.save(Product.builder()
                .name("A4Tech KV-300H Slim szaro-czarna USB")
                .description("Skonstruowana w klasycznym stylu, prosta i wygodna. " +
                        "Popraw ergonomię swojej pracy, wybierając klawiaturę A4Tech KV-300H Slim. " +
                        "Przemyślana konstrukcja pozwala na wygodną pracę, nie męczy nadgarstków i dłoni")
                .category(productCategoryRepository.getOneByName("Klawiatury"))
                .price(BigDecimal.valueOf(94.99))
                .build());

        productRepository.save(Product.builder()
                .name("Zotac GeForce GTX 1650 Gaming AMP CORE GDDR6 4GB")
                .description("Kiedy liczą się FPS-y nie ma czasu na półśrodki, dlatego " +
                        "specjalnie dla Ciebie przetestowaliśmy kartę graficzną z układem " +
                        "GeForce GTX 1650 w konfiguracjach z procesorami Intel i AMD. " +
                        "Przekonaj się, jak to GPU wypada w porównaniu z innymi układami graficznymi " +
                        "z tej półki cenowej, sprawdzając wydajność osiąganą w rozdzielczości Full HD i ustawieniach detali Ultra.")
                .category(productCategoryRepository.getOneByName("Karty graficzne"))
                .price(BigDecimal.valueOf(719))
                .build());

        productRepository.save(Product.builder()
                .name("SilentiumPC Supremo FM2 750W 80 Plus Gold")
                .description("Zasilacz SilentiumPC Supremo FM2 Gold 750 W to najnowsza wersja zasilacza z tej linii, " +
                        "który ma być podstawą do budowy najwydajniejszych komputerów.")
                .category(productCategoryRepository.getOneByName("Zasilacze"))
                .price(BigDecimal.valueOf(439))
                .build());

        log.debug("breakpoint: {}", productCategoryRepository.getOneByName("Klawiatury")
                .getProducts());
    }

    private void loadProductCategoryData() {
        productCategoryRepository.save(ProductCategory.builder()
                .name("Monitory")
                .build());

        productCategoryRepository.save(ProductCategory.builder()
                .name("Klawiatury")
                .build());

        productCategoryRepository.save(ProductCategory.builder()
                .name("Myszki")
                .build());

        productCategoryRepository.save(ProductCategory.builder()
                .name("Procesory")
                .build());

        productCategoryRepository.save(ProductCategory.builder()
                .name("Karty graficzne")
                .build());

        productCategoryRepository.save(ProductCategory.builder()
                .name("Zasilacze")
                .build());
    }

    @Transactional
    public void loadUsersData() {
        userRepository.save(User.builder()
                .username("admin@admin.pl")
                .password(passwordEncoder.encode("admin"))
                .active(true)
                .roles(Set.of("ROLE_ADMIN"))
                .build());

        userRepository.save(User.builder()
                .username("klient1@user.pl")
                .password(passwordEncoder.encode("klient1"))
                .active(true)
                .roles(Set.of("ROLE_USER"))
                .build());

        userRepository.save(User.builder()
                .username("klient2@user.pl")
                .password(passwordEncoder.encode("klient2"))
                .active(true)
                .roles(Set.of("ROLE_USER"))
                .build());
    }

    private void loadUserDetailsData() {
        userDetailsRepository.save(UserDetails.builder()
                .firstName("Andrzej")
                .lastName("Kowalski")
                .zipCode("67-113")
                .town("Wrocław")
                .street("Legnicka")
                .homeNumber(4)
                .flatNumber(12)
                .user(userRepository.getUsersByUsername("admin@admin.pl"))
                .build());

        userDetailsRepository.save(UserDetails.builder()
                .firstName("Tomasz")
                .lastName("Nowak")
                .zipCode("11-331")
                .town("Warszawa")
                .street("Wiejska")
                .homeNumber(1)
                .user(userRepository.getUsersByUsername("klient1@user.pl"))
                .build());

        userDetailsRepository.save(UserDetails.builder()
                .firstName("Halina")
                .lastName("Kopeć")
                .zipCode("99-899")
                .town("Szczyrk")
                .street("Długa")
                .homeNumber(6)
                .flatNumber(3)
                .user(userRepository.getUsersByUsername("klient2@user.pl"))
                .build());
    }


}
