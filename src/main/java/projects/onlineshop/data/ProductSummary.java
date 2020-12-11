package projects.onlineshop.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductSummary {

    private String name;
    private ProductCategorySummary category;
    private BigDecimal price;
}
