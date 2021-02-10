package projects.onlineshop.web.controller.product;

import lombok.With;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import projects.helpers.DataHelper;
import projects.onlineshop.security.CustomUserDetailsService;
import projects.onlineshop.service.ProductCategoryService;
import projects.onlineshop.service.ProductService;
import projects.onlineshop.web.command.CreateProductCommand;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Add new product specification: /product/add")
@WebMvcTest(AddNewProductController.class)
class AddNewProductControllerTest {

    @MockBean
    ProductService productService;
    @MockBean
    ProductCategoryService productCategoryService;
    @MockBean
    CustomUserDetailsService mockCUDS;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    String endpoint = "/product/add";

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("1. On Get Request")
    @Nested
    class GetRequest {

        @DisplayName("- should got add product view on GET request")
        @Test
        @WithMockUser("user@user.pl")
        void test1() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("product/add"))
                    .andExpect(MockMvcResultMatchers.model().attribute("createProductCommand", new CreateProductCommand()));
        }
    }
    @Nested
    @DisplayName("2. On Post Request")
    class PostRequest {

        @DisplayName("- should add product when data is correct")
        @Test
        @WithMockUser("user@user.pl")
        void test1() throws Exception {
        CreateProductCommand createProductCommand = DataHelper.createProductCommand("Awesome new blue keyboard","Keyboard m2", BigDecimal.valueOf(200L),3L);
        when(productService.create(createProductCommand)).thenReturn(10L);

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description","Awesome new blue keyboard")
                .param("name","Keyboard m2")
                .param("price","200")
                .param("categoryId","3")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("list"));

        }
        @DisplayName("- should stay on product/add view with errors for invalid data")
        @ParameterizedTest
        @CsvSource({
                ",,,-10",
                "kop,,,-20"
        })
        @WithMockUser("user@user.pl")
        void test2(String name,String description,String categoryId, String pirce) throws Exception {
            Mockito.clearInvocations(productService);
            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name",name)
                    .param("description",description)
                    .param("categoryId",categoryId)
                    .param("price",pirce)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("product/add"))
                    .andExpect(MockMvcResultMatchers.model().attributeExists("createProductCommand"))
                    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("createProductCommand","name","description","categoryId","price"));
            verify(productService, Mockito.never()).create(ArgumentMatchers.any());
            verifyNoInteractions(productService);

        }

    }
}