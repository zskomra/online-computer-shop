package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.Order;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.domain.model.WatchProduct;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Converter Specification")
class UserConverterTest {

    UserConverter cut;

    @BeforeEach
    void setUp(){
        cut = new UserConverter();
    }

    @DisplayName("1. Converting from registration command")
    @Nested
    class ConvertingFromRegistrationCommand {


        @DisplayName("- should convert valid registration command to user")
        @Test
        void shouldConvertValidRegistrationCommandToUser(){
            RegisterUserCommand command = DataHelper.registerUserCommand("user@user.pl","s3cr3t");

            User result = cut.from(command);

            assertAll("provided values are set and same as command",
                    () -> assertNotNull(result),
                    () -> assertEquals("user@user.pl",result.getUsername()),
                    () -> assertEquals("s3cr3t",result.getPassword()));
        }
        @DisplayName("- should convert to user with default values set")
        @Test
        void shouldConvertToUserWithDefaultValuesSet () {
            RegisterUserCommand command = DataHelper.registerUserCommand("user@user.pl","s3cr3t");

            User result = cut.from(command);

            assertEquals(false,result.getActive());
            assertNull(result.getRoles());
            assertNull(result.getUserDetails());
            assertNull(result.getOrder());
        }

        @DisplayName("- should raise an error when no data provided")
        @Test
        void shouldRaiseAnErrorWhenNoDataProvided(){
            RegisterUserCommand command = null;

            assertThatThrownBy(() -> cut.from(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Register user command cannot be null")
                    .hasNoCause();
        }
    }

    @DisplayName("2. Converting from Edit User Command")
    @Nested
    class ConvertingFromEditUserCommand {

        @DisplayName("- should convert valid user details command to user details")
        @Test
        void shouldConvertValidUserDetailsCommandToUserDetails() {
            EditUserCommand command = DataHelper.editUserCommand("87-140","Chełmża","Bydgoska",
                9,10,"Nowa","Jan" );
            User user = new User(1L,"user@user.pl","s3cr3t",true, Set.of("ROLE_USER"),new UserDetails(),new Order(),new WatchProduct());

            UserDetails expected = DataHelper.userDetails("87-140","Chełmża","Bydgoska",9,10,"Nowa","Jan");
            UserDetails actual = cut.from(command, user);

            assertEquals(expected,actual);
        }

        @DisplayName("- should not change user details when convert from empty edit user command")
        @Test
        void shouldNotChangeUserDetailsWhenConvertFromEmptyEditUserCommand(){
            EditUserCommand command = new EditUserCommand();
            User user = new User(1L,"user@user.pl","s3cr3t",true, Set.of("ROLE_USER"),new UserDetails(),new Order(),new WatchProduct());

            UserDetails actual = cut.from(command, user);
            UserDetails expected = DataHelper.userDetails(null,null,null,null,null,null,null);

            assertEquals(expected,actual);
        }
    }
}