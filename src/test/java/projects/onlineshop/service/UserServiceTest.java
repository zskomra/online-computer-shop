package projects.onlineshop.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import projects.helpers.DataHelper;
import projects.onlineshop.converter.UserConverter;
import projects.onlineshop.domain.model.Order;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.domain.model.WatchProduct;
import projects.onlineshop.domain.repository.UserRepository;
import projects.onlineshop.exception.UnconvertibleDataException;
import projects.onlineshop.exception.UserAlreadyExistsException;
import projects.onlineshop.security.AuthenticatedUser;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import javax.xml.crypto.Data;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Mock
    AuthenticatedUser authenticatedUser;

    @Captor
    ArgumentCaptor<User> userCaptor;
    @Captor
    ArgumentCaptor<EditUserCommand> commandCaptor;


    @InjectMocks
    UserService cut;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("1. Creating new user")
    @Nested
    class CreateUser {

        @DisplayName("- should save user with provided data and set default values and return id")
        @Test
        void shouldSaveUserWithProvidedDataAndSetDefaultValuesAndReturnId(){
            RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user@user.pl","s3cr3t");
            User convertedUser = DataHelper.user("user@user.pl","s3cr3t");
            when(userConverter.from(registerUserCommand)).thenReturn(convertedUser);
            when(userRepository.save(userCaptor.capture()))
                    .thenAnswer(invocation -> {
                        User userToSave =invocation.getArgument(0,User.class);
                        userToSave.setId(10L);
                        return userToSave;
                    });
            when(passwordEncoder.encode("s3cr3t")).thenReturn("encoded");
            when(userRepository.existsByUsername(convertedUser.getUsername())).thenReturn(false);

            Long result = cut.create(registerUserCommand);
            User savedUser = userCaptor.getValue();

            assertNotNull(result);
            assertNotNull(savedUser.getUserDetails());
            assertNotNull(savedUser.getOrder());
            assertNotNull(savedUser.getWatchProduct());
            assertTrue(savedUser.getActive());
            assertThat(savedUser.getRoles()).containsOnly("ROLE_USER");
            assertEquals("encoded",savedUser.getPassword());
        }

        @DisplayName("- should raise an error when user with same username exists")
        @Test
        void shouldRaiseAnErrorWhenUserWithSameUsernameExists(){
            RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user@user.pl","s3cr3t");
            User convertedUser = DataHelper.user("user@user.pl","s3cr3t");

            when(userConverter.from(registerUserCommand)).thenReturn(convertedUser);
            when(userRepository.existsByUsername("user@user.pl")).thenReturn(true);

            assertThatThrownBy(()->cut.create(registerUserCommand))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasNoCause()
                    .hasMessageContaining("Użytkownik user@user.pl już istnieje");
            Mockito.verifyNoMoreInteractions(passwordEncoder);
            Mockito.verifyNoMoreInteractions(userRepository);
        }

        @DisplayName("- should propagate error when command is null")
        @Test
        void shouldPropagateErrorWhenCommandIsNull(){
            UnconvertibleDataException exception = new UnconvertibleDataException("Cannot convert from null");

            Mockito.when(userConverter.from(null)).thenThrow(exception);

            assertThatThrownBy(()->cut.create(null)).isEqualTo(exception);
        }

        @DisplayName("- should propagate error when cannot save")
        @Test
        void shouldPropagateErrorWhenCannotSave(){
            RegisterUserCommand registerUserCommand = DataHelper.registerUserCommand("user","s3cr3t");
            User convertedUser = DataHelper.user("user","s3cr3t");

            when(userConverter.from(registerUserCommand)).thenReturn(convertedUser);
            when(userRepository.existsByUsername("user")).thenReturn(false);

            RuntimeException re = new RuntimeException(new RuntimeException());
            when(userRepository.save(ArgumentMatchers.any())).thenThrow(re);

            assertThatThrownBy(() -> cut.create(registerUserCommand))
                    .isInstanceOf(RuntimeException.class)
                    .hasCauseInstanceOf(RuntimeException.class);
        }

    }

    @DisplayName("2. Edit user details")
    @Nested
    class EditUserDetails {

        @DisplayName("- should return changed user details from edit user command")
        @Test
        void shouldReturnChangedUserDetailsFromEditUserCommand() {
            User user = DataHelper.user(2L,"user@user.pl","password",new UserDetails(),new Order(),true,Set.of("ROLE_USER"),new WatchProduct());
            EditUserCommand editUserCommand = DataHelper.editUserCommand("87-140", "Chełmża", "Bydgoska",
                5, 15, "Kowalski", "Jan");

            when(authenticatedUser.getUsername()).thenReturn("user@user.pl");
            when(userRepository.getUsersByUsername("user@user.pl")).thenReturn(user);
            when(userConverter.from(any(EditUserCommand.class),any(User.class)))
                    .thenAnswer(invocation -> {
                        EditUserCommand userCommand = invocation.getArgument(0);
                        User userToEdit = invocation.getArgument(1);
                        UserDetails userDetails = userToEdit.getUserDetails();
                        userDetails.setTown(userCommand.getTown());
                        userDetails.setZipCode(userCommand.getZipCode());
                        userDetails.setStreet(userCommand.getStreet());
                        userDetails.setLastName(userCommand.getLastName());
                        userDetails.setFirstName(userCommand.getFirstName());
                        userDetails.setHomeNumber(userCommand.getHomeNumber());
                        userDetails.setFlatNumber(userCommand.getFlatNumber());
                        return userToEdit.getUserDetails();
                    });
            UserDetails expected = DataHelper.userDetails("87-140", "Chełmża", "Bydgoska",
                    5, 15, "Kowalski", "Jan");

            boolean result = cut.editUserDetails(editUserCommand);
            assertTrue(result);
            assertEquals(expected,user.getUserDetails());

        }

    }
}
