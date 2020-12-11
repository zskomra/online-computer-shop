package projects.onlineshop.web.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ContactCommand {

    @NotBlank @Size(min = 5)
    private String topic;
    @Email
    private String email;
    @NotBlank @Size(min = 5)
    private String description;
}
