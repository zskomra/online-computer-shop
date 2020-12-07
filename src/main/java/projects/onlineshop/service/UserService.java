package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.UserConverter;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.exception.UserAlreadyExistsException;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(RegisterUserCommand registerUserCommand) {
        User userToCreate = userConverter.from(registerUserCommand);
        log.debug("Dane użytkownika do zapisu: {}", registerUserCommand);
        if(userRepository.existsByUsername(userToCreate.getUsername())){
            throw new UserAlreadyExistsException(String.format("Użytkownik %s już istnieje", userToCreate.getUsername()));
        }
        userToCreate.setActive(true);
        userToCreate.setRoles(Set.of("ROLE_USER"));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userRepository.save(userToCreate);
        log.debug("Zapisano użytkownika: {}", userToCreate);
        return userToCreate.getId();
    }

    @Transactional
    public boolean editUserDetails(EditUserCommand editUserCommand) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userToEdit = userRepository.getUsersByUsername(username);
        userConverter.from(editUserCommand, userToEdit);
        return true;
    }
}
