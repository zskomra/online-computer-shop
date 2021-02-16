package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.UserConverter;
import projects.onlineshop.domain.model.order.Order;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.domain.model.WatchProduct;
import projects.onlineshop.domain.model.order.UserOrder;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.exception.UserAlreadyExistsException;
import projects.onlineshop.security.AuthenticatedUser;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUser authenticatedUser;

    @Transactional
    public Long create(RegisterUserCommand registerUserCommand) {
        log.debug("Dane użytkownika do zapisu: {}", registerUserCommand);

        User userToCreate = userConverter.from(registerUserCommand);
        log.debug("Dane użytkownika do zapisu: {}", userToCreate);
        if(userRepository.existsByUsername(userToCreate.getUsername())){
            throw new UserAlreadyExistsException(String.format("Użytkownik %s już istnieje", userToCreate.getUsername()));
        }
        userToCreate.setActive(true);
        userToCreate.setRoles(Set.of("ROLE_USER"));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userToCreate.setUserDetails(UserDetails.builder()
                .user(userToCreate)
                .build());
        userToCreate.setOrder(Order.builder()
                .user(userToCreate)
                .products(new ArrayList<>())
                .build());
        userToCreate.setWatchProduct(WatchProduct.builder()
                .user(userToCreate)
                .products(new HashSet<>())
                .build());
        userRepository.save(userToCreate);
        log.debug("Zapisano użytkownika: {}", userToCreate);
        return userToCreate.getId();
    }

    @Transactional
    public boolean editUserDetails(EditUserCommand editUserCommand) {
        String username = authenticatedUser.getUsername();
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userToEdit = userRepository.getUsersByUsername(username);
        log.debug("Pobrano uzytkownika do edycji : {}", username);
        userConverter.from(editUserCommand, userToEdit);
        log.debug("Zmienione dane uzytkownika : {}", userToEdit.getUserDetails());
        return true;
    }

    public UserDetails getCurrentUserDetails() {
        String username = authenticatedUser.getUsername();
        User user = userRepository.getUsersByUsername(username);
        return user.getUserDetails();
    }

    public User getLoggedUser() {
        String username = authenticatedUser.getUsername();
        return userRepository.getUsersByUsername(username);
    }
}
