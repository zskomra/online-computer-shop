package projects.onlineshop.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projects.onlineshop.domain.model.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSummary {

    private Long id;
    private Integer rating;
    private String title;
    private String opinion;
    private String dateComment;
    private Product product;
    private String username;
}
