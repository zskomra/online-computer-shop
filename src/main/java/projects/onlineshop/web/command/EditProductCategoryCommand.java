package projects.onlineshop.web.command;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EditProductCategoryCommand {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;
}
