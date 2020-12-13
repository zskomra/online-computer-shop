package projects.onlineshop.web.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRatingCommand {

    @NotNull
    private Integer rating;

    @Size(max = 160)
    private String opinion;

    @NotNull
    private LocalDate datePurchase;

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;
}
