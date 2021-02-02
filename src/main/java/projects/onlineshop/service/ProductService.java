package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ProductConverter;
import projects.onlineshop.data.ProductSummary;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.ProductCategoryRepository;
import projects.onlineshop.domain.repository.ProductRepository;
import projects.onlineshop.domain.repository.RatingRepository;
import projects.onlineshop.exception.ProductAlreadyExistsException;
import projects.onlineshop.web.command.CreateProductCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final ProductCategoryRepository productCategoryRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public Long create(CreateProductCommand product) {
        log.debug("Dane do utworzenia produktu: {}", product);

        if (productRepository.existsByName(product.getName())) {
            log.debug("Produkt o podanej nazwie istnieje w bazie");
            throw new ProductAlreadyExistsException(
                    String.format("Produkt o nazwie %s już istnieje", product.getName()));
        }

        Product productToSave = productConverter.from(product);
        productToSave.setAddDate(LocalDate.now());
        log.debug("Dodane date utworzenia: {}", productToSave.getAddDate());
        productRepository.save(productToSave);
        log.debug("Zapisano produkt: {}", productToSave);

        return productToSave.getId();
    }

    @Transactional
    public Page<Product> getAllProducts(int pageNum, String sortField, String sortDir) {
        Pageable pageable = getSortedPageable(pageNum, sortField, sortDir);

        return productRepository.findAll(pageable);
    }

    private Pageable getSortedPageable(int pageNum, String sortField, String sortDir) {
        int pageSize = 10;
        Pageable pageable;

        if (sortField.equals("category")) {
            pageable = PageRequest.of(pageNum - 1, pageSize,
                    sortDir.equals("asc") ? Sort.by("category.name").ascending() : Sort.by("category.name").descending());
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize,
                    sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        }

        return pageable;
    }

    @Transactional
    public Page<Product> getAllProductsFiltered(int pageNum, String sortField, String sortDir, String regex) {
        Pageable sortedPageable = getSortedPageable(pageNum, sortField, sortDir);

        String trimRegex = regex.trim();
        return productRepository.findAllByNameContaining(sortedPageable, trimRegex);
    }

    public List<ProductSummary> mapProductsToProductsSummaries(Page<Product> allProducts) {
        return allProducts.stream()
                .map(productConverter::fromProductToProductSummary)
                .collect(Collectors.toList());
    }

    public Product getProductById(Long id) {
        Product product = productRepository.getById(id);
        log.debug("Pobrano produkt: {}", product);
        return product;
    }

    public List<Product> getProductsByCategory(Long productCategoryId) {
        return productRepository.getAllByCategoryId(productCategoryId);
    }

    public List<Product> getLast5AddedProductsSortedByDate() {

        List<Product> allProducts = productRepository.findTop5ByOrderByAddDateDesc();

        return allProducts;
    }

<<<<<<< HEAD

    public List<Product> top5ratedProducts (){
        return productRepository.findTop5RatedProducts(PageRequest.of(0,5));

            }
    }

=======
    public Set<Product> getRecommend() {

        Set<Product> productToRecommend = new HashSet<>();

        productToRecommend.add(randomProductToSet());
        int count = 0;

        while (count < 3) {
            Product temporary = randomProductToSet();
            if (!isEquals(temporary, productToRecommend)) {
                productToRecommend.add(temporary);
                count++;
            }
        }
        return productToRecommend;
    }

    private boolean isEquals(Product temporary, Set<Product> productToRecommend) {
        for (Product product : productToRecommend) {
            if (temporary.getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }

    private Product randomProductToSet() {
        List<Product> allProducts = productRepository.findAll();
        Random randIndex = new Random();

        return allProducts.get(randIndex.nextInt(allProducts.size()));
    }
}
>>>>>>> #12 opsg :: add panel with random choice product for user
