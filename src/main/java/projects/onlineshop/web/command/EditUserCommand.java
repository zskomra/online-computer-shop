package projects.onlineshop.web.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
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
    @NotNull
    private Integer homeNumber;
    private Integer flatNumber;

    }
