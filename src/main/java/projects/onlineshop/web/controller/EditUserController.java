package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.web.command.EditUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor @Slf4j
public class EditUserController {

    @GetMapping
    public String getUserProfilePage (Model model){
        model.addAttribute(new EditUserCommand());
        return "/profile";
    }

    @PostMapping
    public String processEditUserProfile(@Valid EditUserCommand editUserCommand) {
        
        return "";
    }

}
