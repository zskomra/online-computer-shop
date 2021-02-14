package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.service.OrderService;
import projects.onlineshop.service.UserService;
import projects.onlineshop.web.command.EditUserCommand;

import javax.validation.Valid;
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
        showUserCart(model);
        return "order/list";
    }

    private void showUserCart(Model model) {
        List<Product> orders = userService.getLoggedUser().getOrder().getProducts();
        Order order = userService.getLoggedUser().getOrder();
        BigDecimal bigDecimal = orderService.getOrderSum();
        log.debug("ilosc zamowien: {}", orders.size());
        model.addAttribute("orderSum", bigDecimal);
        model.addAttribute("userOrders",orders );
        model.addAttribute("order",order);
    }

    @GetMapping("/confirm")
    public String showOrderSummary(Model model) {
        showUserCart(model);
        EditUserCommand editUserCommand = showCurrentUserDetails();
        model.addAttribute("editUserCommand",editUserCommand);
        return "order/order-summary";
    }
    @PostMapping("/confirm")
    public String processOrder(@Valid EditUserCommand editUserCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug("Niepoprawne dane do wysyłki: {}", editUserCommand);
            return "order/order-summary";
        }
        return "order/order-confirm";
    }

    @GetMapping("confirm/summary")
    public String showSummary() {
        return "order/order-confirm";
    }

//    @RequestMapping(value = "/confirm", method = {RequestMethod.GET, RequestMethod.POST})
//    public String confirmPurchase(@RequestParam Long orderId, Model model,  EditUserCommand editUserCommand){
//        Long id = orderId;
//        log.debug("Pobrano id zamowienia: {}", id);
//        showUserCart(model);
//        editUserCommand = showCurrentUserDetails();
//        model.addAttribute("editUserCommand",editUserCommand);


//        Boolean success = orderService.confirmOrder(orderId);
//        log.debug("Koszyk usunieto : {}", success);
//        return "order/order-summary";
//    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteProductFromOrder(@RequestParam Long productId) {
        orderService.deleteProductFromCart(productId);
        return "redirect:/order/list";
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
}





