package projects.onlineshop.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import projects.onlineshop.security.CustomUserDetailsService;
import projects.onlineshop.service.ContactService;
import projects.onlineshop.web.command.ContactCommand;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Prepare message specification: /contact")
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @MockBean
    ContactService contactService;
    @MockBean
    CustomUserDetailsService cUDS;

    @Autowired
    MockMvc mockMvc;

    String endpoint = "/contact";

    @Nested
    @DisplayName("1. On Get Request")
    class GetRequest {

        @Test
        @DisplayName("- should got contact view on GET request")
        void test1() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("contact/index"))
                    .andExpect(MockMvcResultMatchers.model().attribute("contactCommand",new ContactCommand()));
        }
    }

    @DisplayName("2. On Post Request")
    @Nested
    class PostRequest {

        @Test
        @DisplayName("- should send message when data is correct")
        void test1() throws Exception {
            ContactCommand contactCommand = new ContactCommand();
            contactCommand.setDescription("Some description");
            contactCommand.setEmail("user@user.pl");
            contactCommand.setTopic("Topic");

            when(contactService.send(contactCommand)).thenReturn(true);

            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("description","Some description")
                    .param("topic","Topic")
                    .param("email","user@user.pl")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.view().name("contact/confirm"));
        }
        @DisplayName("-should stay on /contact view with errors for invalid data")
        @ParameterizedTest
        @CsvSource({
                ",,",
                "user,xxx,desc"
        })
        void test2(String email, String topic, String description) throws Exception {
            Mockito.clearInvocations(contactService);
            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("email",email)
                    .param("topic",topic)
                    .param("description",description)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("contact/index"))
                    .andExpect(MockMvcResultMatchers.model().attributeExists("contactCommand"))
                    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contactCommand","email","topic","description"));

            verify(contactService, never()).send(ArgumentMatchers.any());
            verifyNoInteractions(contactService);
        }
    }

}