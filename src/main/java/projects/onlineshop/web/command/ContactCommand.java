package projects.onlineshop.web.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactCommand {

    @NotBlank @Size(min = 5)
    private String topic;
    @Email
    private String email;
    @NotBlank @Size(min = 5)
    private String description;
}
