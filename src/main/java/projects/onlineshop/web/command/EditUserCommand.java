package projects.onlineshop.web.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EditUserCommand {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Size(min = 6,max = 6)
    private String zipCode;
    @NotBlank
    private String town;
    @NotBlank
    private String street;
    @NotBlank
    private Integer homeNumber;
    private Integer flatNumber;

    }
