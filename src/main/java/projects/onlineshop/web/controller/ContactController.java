package projects.onlineshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.onlineshop.service.ContactService;
import projects.onlineshop.web.command.ContactCommand;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public String getContact(Model model) {
        model.addAttribute(new ContactCommand());
        return "contact/index";
    }

    @PostMapping
    public String processSendMessage(@Valid ContactCommand contactCommand, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.debug("Błędne dane do kontaktu: {}", contactCommand);
            return "contact/index";
        }
        try {
            Boolean success = contactService.send(contactCommand);
            log.debug("Wiadomosc zostala wyslana i zapisana: {}", success);

            return "contact/confirm";
        }
        catch (RuntimeException | MessagingException re) {
            bindingResult.rejectValue(null, null, "Wystapil blad");
            return "contact/index";
        }

    }


}
