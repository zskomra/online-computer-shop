package projects.onlineshop.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller @Slf4j
@RequestMapping("/home")
public class HomePageController {

    @GetMapping
    public String getMainPage() {
        return "home/main";
    }
}
