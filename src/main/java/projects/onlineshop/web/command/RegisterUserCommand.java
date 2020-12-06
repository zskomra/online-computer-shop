package projects.onlineshop.web.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class RegisterUserCommand {

    @NotNull @Email
    private String username;
    @NotBlank
    private String password;
}
