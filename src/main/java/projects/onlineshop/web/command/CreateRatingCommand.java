package projects.onlineshop.web.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRatingCommand {

    @NotNull
    private Integer rating;

    @NotNull
    @Size(max = 24)
    private String title;

    @Size(max = 160)
    private String opinion;
    private Long currentProductId;
}
