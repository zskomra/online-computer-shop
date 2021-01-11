package projects.onlineshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Contact service specification")
@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    ContactConverter contactConverter;

    @Mock
    ContactRepository contactRepository;


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
    void send() throws MessagingException {
        ContactCommand command = DataHelper.contactCommand("Temat", "Przykładowy opis","user@user.pl");
        Contact contact = DataHelper.contact("Temat", "Przykładowy opis","user@user.pl",
                String.format("From user: %s \nProblem desctiption: %s","user@user.pl","Przykładowy opis"));
        ContactConverter contactConverter = Mockito.mock(ContactConverter.class);
        Mockito.lenient().when(contactConverter.from(command)).thenReturn(contact);

        MimeMessage mimeMessage = new MimeMessage((Session)null);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = Mockito.mock(MimeMessageHelper.class);
        mimeMessageHelper.setFrom(contact.getEmail());
        mimeMessageHelper.setTo("exampletestacc36@gmail.com");
        mimeMessageHelper.setSubject(contact.getTopic());
        mimeMessageHelper.setText(contact.getContentForAdmin());
        mimeMessageHelper.setSentDate(new Date());



        Mockito.when(contactRepository.save(contact)).thenAnswer(invocation -> {
            Contact contactToSave = invocation.getArgument(0, Contact.class);
            contactToSave.setId(1L);
            return contactToSave;
        });


        Boolean success = cut.send(command);
//
//        assertTrue(success);

    }
}