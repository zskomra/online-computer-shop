package projects.onlineshop.web.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.onlineshop.domain.model.ProductCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductCommand {

    @NotNull @Size(min = 5, max = 30)
    private String name;

    @Size(max = 250)
    private String description;

    @NotNull
    private CreateProductCategoryCommand category;

    @Positive
    private BigDecimal price;
}
