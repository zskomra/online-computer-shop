package projects.onlineshop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping("/home")
    public String getMainPage() {
        return "home/main";
    }
}
