package projects.onlineshop.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.exception.UserAlreadyExistsException;
import projects.onlineshop.service.UserService;
import projects.onlineshop.web.command.RegisterUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@Slf4j @RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String getRegister(Model model){
        model.addAttribute(new RegisterUserCommand());
        return "register/form";
    }

    @PostMapping
    public String processRegister(@Valid RegisterUserCommand registerUserCommand, BindingResult bindingResult) {
        log.debug("Dane do utworzenia użytkownika: {}", registerUserCommand);
        if(bindingResult.hasErrors()) {
            log.debug("Błędne dane użytkownika: {}" , registerUserCommand);
            return "register/form";
        }
        try {
            Long id = userService.create(registerUserCommand);
            log.debug("Utworzono użytkownika: {}", id);
            return "redirect:/login";
        }
        catch (UserAlreadyExistsException uaee) {
            bindingResult.rejectValue("username",null,"Użytkownik o podanym adresie już istnieje");
            return "register/form";
        }
        catch (RuntimeException re){
        bindingResult.rejectValue(null, null, "Wystąpił błąd");
        return "register/form";
        }

    }

}
