package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.Contact;
import projects.onlineshop.web.command.ContactCommand;
import projects.onlineshop.web.command.CreateProductCommand;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Contact Converter Specification")
class ContactConverterTest {

    ContactConverter cut;

    @BeforeEach
    void setUp() {
        cut = new ContactConverter();
    }

    @Test
    @DisplayName("- should convert valid contact message")
    void shouldConvertValidContactMessage () {
        ContactCommand command = DataHelper.contactCommand("Temat", "Przykładowy opis","user@user.pl");

        Contact expected = DataHelper.contact("Temat", "Przykładowy opis","user@user.pl",
                String.format("From user: %s \nProblem desctiption: %s","user@user.pl","Przykładowy opis"));

        Contact result = cut.from(command);

        assertEquals(expected,result);


    }

    @Test
    @DisplayName("- should raise exception when converting from null")
    void shouldRaiseExceptionWhenConvertingFromNull() {
        ContactCommand command = null;

        assertThrows(IllegalArgumentException.class, () -> cut.from(command));
    }

}