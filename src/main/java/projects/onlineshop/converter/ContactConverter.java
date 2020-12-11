package projects.onlineshop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.Contact;
import projects.onlineshop.web.command.ContactCommand;

@Component
@RequiredArgsConstructor
public class ContactConverter {


    public Contact from(ContactCommand contactCommand) {
        return Contact.builder()
                .email(contactCommand.getEmail())
                .topic(contactCommand.getTopic())
                .description(contactCommand.getDescription())
                .contentForAdmin(String.format("From user: %s \nProblem desctiption: %s",contactCommand.getEmail(),contactCommand.getDescription()))
                .build();

    }
}
