package projects.onlineshop.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.service.UserService;
import projects.onlineshop.web.command.RegisterUserCommand;

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
    public String processRegister(RegisterUserCommand registerUserCommand, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register/form";
        }
        try {
            Long id = userService.create(registerUserCommand);
            return "redirect:/login";
        }
        catch (RuntimeException re){
        bindingResult.rejectValue(null, null, "Wystąpił błąd");
        return "register/form";
        }

    }

}
