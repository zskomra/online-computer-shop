package projects.onlineshop.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import projects.helpers.DataHelper;
import projects.onlineshop.converter.UserConverter;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.exception.UserAlreadyExistsException;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("User service specification")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserConverter userConverter;


    @InjectMocks
    UserService cut;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldInjectsAllMocks() {
        assertNotNull(cut);
    }

    @Test
    @DisplayName("-should return success response for valid registration command")
    void shouldReturnSuccessResponseForValidRegistrationCommand() {
        RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user@user.pl", "pa55word");
        User user = DataHelper.user("user@user.pl", "pa55word", new UserDetails());
            when(userConverter.from(registerUserCommand)).thenReturn(user);
        when(userRepository.existsByUsername("user@user.pl")).thenReturn(false);
        when(userRepository.save(user)).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0, User.class);
            userToSave.setId(1L);
            return userToSave;
        });
        Long id = cut.create(registerUserCommand);

        assertNotNull(id);
        assertEquals(user.getId(), id);
    }


    @Test
    @DisplayName("- should return unsuccessful response when username already exists")
    void shouldReturnUnsuccessfulResponseWhenUsernameAlreadyExists() {
        RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user@user.pl", "pa55word");
        User user = DataHelper.user("user@user.pl", "pa55word", new UserDetails());
        when(userConverter.from(registerUserCommand)).thenReturn(user);
        when(userRepository.existsByUsername("user@user.pl")).thenReturn(false);
        when(userRepository.save(user)).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0, User.class);
            userToSave.setId(1L);
            return userToSave;
        });
        Long id = cut.create(registerUserCommand);

        when(userRepository.existsByUsername("user@user.pl")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> cut.create(registerUserCommand));
    }

    @Test
    @DisplayName("- should save user with expected credentials")
    void shouldSaveUserWithExpectedCredentials() {
        RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user@user.pl", "pa55word");
        User user = DataHelper.user("user@user.pl", "pa55word", new UserDetails());
        when(userConverter.from(registerUserCommand)).thenReturn(user);
        when(userRepository.existsByUsername("user@user.pl")).thenReturn(false);
//        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn();
        when(userRepository.save(user)).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0, User.class);
            userToSave.setId(1L);
            return userToSave;
        });

        Long id = cut.create(registerUserCommand);

        assertEquals(user.getActive(), true);
        assertEquals(user.getRoles(), Set.of("ROLE_USER"));


    }

    @Test
    @DisplayName("- should return unsuccessful response when command cannot be converted")
    void shouldReturnUnsuccessfulResponseWhenCommandCannotBeConverted() {
        RegisterUserCommand registerUserCommand = null;
        when(userConverter.from(registerUserCommand)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> cut.create(registerUserCommand));
    }

    @Test
    @DisplayName("-should return changed user details from edit user command ")
    void shouldReturnChangedUserDetailsFromEditUserCommand() {
        User user = DataHelper.user("user@user.pl", "password", new UserDetails());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.lenient().when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getUsername());
        when(userRepository.getUsersByUsername("user@user.pl")).thenReturn(user);

        EditUserCommand editUserCommand = DataHelper.editUserCommand("87-140", "Chełmża", "Bydgoska",
                5, 15, "Kowalski", "Jan");
        UserDetails expected = DataHelper.userDetails("87-140", "Chełmża", "Bydgoska",
                5, 15, "Kowalski", "Jan");
        Mockito.when(userConverter.from(editUserCommand, user)).thenAnswer(invocation -> {
            user.setUserDetails(expected);
            return user.getUserDetails();
        });

        boolean success = cut.editUserDetails(editUserCommand);
        assertTrue(success);
    }


    @Test
    @DisplayName("-should return current logged user")
    void shouldReturnCurrentLoggedUser() {

        User user = DataHelper.user("user@user.pl", "password", new UserDetails());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.lenient().when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getUsername());
        when(userRepository.getUsersByUsername("user@user.pl")).thenReturn(user);

        String username = cut.getLoggedUser().getUsername();
        assertEquals(user.getUsername(),username);

    }
}




