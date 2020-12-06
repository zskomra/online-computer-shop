package projects.onlineshop.converter;

import org.springframework.stereotype.Component;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.web.command.RegisterUserCommand;

@Component
public class UserConverter {


    public User from(RegisterUserCommand registerUserCommand) {
        return User.builder()
                .username(registerUserCommand.getUsername())
                .password(registerUserCommand.getPassword())
                .build();
    }
}
