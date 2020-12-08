package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.service.UserService;
import projects.onlineshop.web.command.EditUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor @Slf4j
public class EditUserController {

    private final UserService userService;



    @GetMapping
    public String getUserProfilePage (Model model){
        EditUserCommand editUserCommand = showCurrentUserDetails();
        model.addAttribute("editUserCommand",editUserCommand);
        return "profile/form";
    }

    private EditUserCommand showCurrentUserDetails() {
        UserDetails userDetails = userService.getCurrentUserDetails();
        return EditUserCommand.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .flatNumber(userDetails.getFlatNumber())
                .homeNumber(userDetails.getHomeNumber())
                .street(userDetails.getStreet())
                .town(userDetails.getTown())
                .zipCode(userDetails.getZipCode())
                .build();
    }

    @PostMapping
    public String processEditUserProfile(@Valid EditUserCommand editUserCommand, BindingResult bindingResult) {
        log.debug("Pobranie danych do edycji: {}", editUserCommand);
        if(bindingResult.hasErrors()) {
            log.debug("Niepoprawne dane do edycji: {}", editUserCommand);
            return "profile/form";
        }
        try {
            boolean isEditSuccess = userService.editUserDetails(editUserCommand);
            log.debug("Zmieniono dane uzytkownika : {}", isEditSuccess);
            return "redirect:/profile";
        }
        catch (RuntimeException re) {
            log.debug("Blad podczas zmiany danych");
            bindingResult.rejectValue(null,null,"Wystapil blad");

        }
        return "redirect:/profile";
    }

}
