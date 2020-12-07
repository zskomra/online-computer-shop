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
        model.addAttribute(new EditUserCommand());
        return "profile/form";
    }

    @PostMapping
    public String processEditUserProfile(@Valid EditUserCommand editUserCommand, BindingResult bindingResult) {
        log.debug("Pobranie danych do edycji: {}", editUserCommand);
        if(bindingResult.hasErrors()) {
            log.debug("Niepoprawne dane do edycji: {}", editUserCommand);
            return "/profile/form";
        }
        boolean isEditSuccess = userService.editUserDetails(editUserCommand);
        return "redirect:/profile";
    }

}
