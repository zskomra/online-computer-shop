package projects.onlineshop.web.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.WatchProduct;
import projects.onlineshop.service.UserService;
import projects.onlineshop.service.WatchProductService;

import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/watch")
public class WatchProductController {

    private final WatchProductService watchProductService;
    private final UserService userService;


    @GetMapping("/saved")
    public String showWatchedList(Model model) {
        Set<Product> watchedList = userService.getLoggedUser().getWatchProduct().getProducts();
        WatchProduct watchProduct = userService.getLoggedUser().getWatchProduct();
        log.debug("Wielkość listy: {}", watchedList.size());
        model.addAttribute("watchList", watchedList);
        model.addAttribute("watchProducts", watchProduct);
        return "watch/saved";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmdelete(@RequestParam Long productId){
        Long id = productId;
        log.debug("Pobrano id produktu: {}", id);
        Boolean success = watchProductService.confirmDeleteProduct(productId);
        log.debug("Koszyk usunieto : {}", success);
        return "redirect:/watch/saved";
    }
}
