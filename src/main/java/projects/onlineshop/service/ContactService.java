package projects.onlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.onlineshop.converter.ContactConverter;
import projects.onlineshop.domain.model.Contact;
import projects.onlineshop.domain.repository.ContactRepository;
import projects.onlineshop.web.command.ContactCommand;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService{

    private final JavaMailSender javaMailSender;
    private final ContactConverter contactConverter;
    private final ContactRepository contactRepository;

    @Transactional
    public Boolean send(ContactCommand contactCommand) throws MessagingException {
        Contact contactToSave = convertUserInput(contactCommand);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(contactToSave.getEmail());
        mimeMessageHelper.setTo("exampletestacc36@gmail.com");
        mimeMessageHelper.setSubject(contactToSave.getTopic());
        mimeMessageHelper.setText(contactToSave.getContentForAdmin());
        mimeMessageHelper.setSentDate(new Date());
        javaMailSender.send(mimeMessage);
        contactRepository.save(contactToSave);
        log.debug("Zapisano wiadomosc: {}",contactToSave);
        return true;
    }

    private Contact convertUserInput(ContactCommand contactCommand) {
        return contactConverter.from(contactCommand);

    }


    }


