package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.model.UserDetails;
import projects.onlineshop.web.command.EditUserCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Converter Specification")
class UserConverterTest {

    UserConverter cut;

    @BeforeEach
    void setUp(){
        cut = new UserConverter();
    }

    @Test
    @DisplayName("- should convert valid registration command to user")
    void shouldConvertValidRegistrationCommand () {
        RegisterUserCommand command = DataHelper.registerUserCommand("user@user.pl","user");

        User result = cut.from(command);

        User expected = DataHelper.user("user@user.pl","user", new UserDetails());

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("- should raise exception when converting from null")
    void shouldRaiseExceptionWhenConvertingFromNull() {
        RegisterUserCommand command = null;

        assertThrows(IllegalArgumentException.class, () -> cut.from(command));
    }


    @Test
    @DisplayName("- should convert valid user details command to user details")
    void shouldConvertValidUserDetailsCommandToUserDetails() {
        User expectedUser = DataHelper.user("user@user.pl","user", new UserDetails());

        EditUserCommand command = DataHelper.editUserCommand("87-140","Chełmża","Bydgoska",
                9,10,"Nowa","Jan" );

        UserDetails expected = DataHelper.userDetails("87-140","Chełmża","Bydgoska",
                9,10,"Nowa","Jan" );

        UserDetails result = cut.from(command , expectedUser);

        assertEquals(expected,result);

    }
}