package projects.onlineshop.web.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import projects.helpers.DataHelper;
import projects.onlineshop.security.CustomUserDetailsService;
import projects.onlineshop.service.UserService;
import projects.onlineshop.web.command.RegisterUserCommand;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Registration specification: /register")
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    CustomUserDetailsService mockCUDS;

    @Autowired
    MockMvc mockMvc;

    String endpoint = "/register";


    @Nested
    @DisplayName("1. on Get request")
    class GetRequest {
        @Disabled
        @DisplayName("- should got registration view on GET request")
        @Test
        void test1() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("register/form"));
//                    .andExpect(MockMvcResultMatchers.model().attribute("data",new RegisterUserCommand()));
        }
        @Disabled
        @DisplayName("- should allow anonymous user")
        @Test
        void test2() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                    .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @DisplayName("2. On POST request")
    @Nested
    class PostRequest {

        @Test
        @DisplayName("- should create user when data is correct")
        void test1() throws Exception {
            RegisterUserCommand command = DataHelper.registerUserCommand("user@user.pl","s3cr3t");
            when(userService.create(command)).thenReturn(10L);

            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username","user@user.pl")
                    .param("password","s3cr3t")
                    .with(SecurityMockMvcRequestPostProcessors.anonymous())
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
            verify(userService,times(1)).create(command);

        }
        @Disabled
        @DisplayName("- should stay on registration view with errors for invalid data")
        @ParameterizedTest
        @CsvSource({
                "user, x",
                ",",
                "124,"
        })
        void test2(String username, String password) throws Exception {
            Mockito.clearInvocations(userService);
            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username",username)
                    .param("password",password)
                    .with(SecurityMockMvcRequestPostProcessors.anonymous())
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("register/form"))
                    .andExpect(MockMvcResultMatchers.model().attributeExists("data"))
                    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("data","username","password"));
        }
    }
}