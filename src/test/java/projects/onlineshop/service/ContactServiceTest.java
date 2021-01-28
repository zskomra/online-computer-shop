package projects.onlineshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import projects.helpers.DataHelper;
import projects.onlineshop.converter.ContactConverter;
import projects.onlineshop.domain.model.Contact;
import projects.onlineshop.domain.model.User;
import projects.onlineshop.domain.repository.ContactRepository;
import projects.onlineshop.web.command.ContactCommand;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Contact service specification")
@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    ContactConverter contactConverter;

    @Mock
    ContactRepository contactRepository;

    @Captor
    ArgumentCaptor<Contact> contactCaptor;

    @InjectMocks
    ContactService cut;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldInjectsAllMocks() {
        assertNotNull(cut);
    }

    @Test
    @DisplayName("- should send mail when provided data are correct")
    void shouldSendMailWhenProvidedDataAreCorrect() throws MessagingException {
        ContactCommand command = DataHelper.contactCommand("Temat", "Przykładowy opis","user@user.pl");
        Contact contact = DataHelper.contact("Temat", "Przykładowy opis","user@user.pl",
                String.format("From user: %s \nProblem desctiption: %s","user@user.pl","Przykładowy opis"));

        Mockito.when(contactConverter.from(command)).thenReturn(contact);
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.when(contactRepository.save(contactCaptor.capture()))
                .thenAnswer(invocation -> {
                    Contact contactToSave = invocation.getArgument(0, Contact.class);
                    contactToSave.setId(1L);
                    return contactToSave;
        });

        Boolean success = cut.send(command);
        Contact contactSaved = contactCaptor.getValue();

        verify(javaMailSender).send(mimeMessage);
        verify(javaMailSender,times(1)).send(mimeMessage);
        assertTrue(success);
        assertEquals(1L,contactSaved.getId());
    }

    @Test
    @DisplayName("- should return unsuccessful response when contact command cannot be converted")
    void shouldReturnUnsuccessfulResponseWhenContactCommandCannotBeConverted() throws MessagingException {
        when(contactConverter.from(null)).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> cut.send(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasNoCause();

        verifyNoInteractions(javaMailSender);
        verifyNoInteractions(contactRepository);


    }
}