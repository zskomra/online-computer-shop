package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.service.OrderService;
import projects.onlineshop.service.UserService;

import java.math.BigDecimal;
import java.util.List;


@Controller @Slf4j @RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;



    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addProductToOrder(@RequestParam Long productId) {
        Long id = productId;
        log.debug("Pobrano id produktu: {}", id);
        orderService.addOrder(id);
        return "redirect:/order/list";
    }



    @GetMapping("/list")
    public String showOrders(Model model) {
        List<Product> orders = userService.getLoggedUser().getOrder().getProducts();
        Order order = userService.getLoggedUser().getOrder();
        BigDecimal bigDecimal = orderService.getOrderSum();
        log.debug("ilosc zamowien: {}", orders.size());
        model.addAttribute("orderSum", bigDecimal);
        model.addAttribute("userOrders",orders );
        model.addAttribute("order",order);
        return "order/list";
    }

    @RequestMapping(value = "/purchase", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmPurchase(@RequestParam Long orderId){
        Long id = orderId;
        log.debug("Pobrano id zamowienia: {}", id);
        Boolean success = orderService.confirmOrder(orderId);
        log.debug("Koszyk usunieto : {}", success);
        return "redirect:/order/list";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteProductFromOrder(@RequestParam Long productId) {
        orderService.deleteProductFromCart(productId);
        return "redirect:/order/list";
    }
}





