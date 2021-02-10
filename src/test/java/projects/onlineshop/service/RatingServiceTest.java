package projects.onlineshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import projects.helpers.DataHelper;
import projects.onlineshop.converter.RatingConverter;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.domain.repository.RatingRepository;
import projects.onlineshop.security.AuthenticatedUser;
import projects.onlineshop.web.command.CreateRatingCommand;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Project process specification")
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    RatingConverter ratingConverter;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    AuthenticatedUser authenticatedUser;

    @InjectMocks
    RatingService cut;

    @Nested
    @DisplayName("1. Create rating")
    class CreateRating {

        @Captor
        ArgumentCaptor<ProductRating> ratingCaptor;

        @Test
        @DisplayName("- should add rating to product with provided data")
        void test1() {
            CreateRatingCommand ratingCommand = CreateRatingCommand.builder().currentProductId(10L).title("Title").opinion("Awesome opinion").rating(4).build();
            Product product = DataHelper.product(10L,"Mouse","Super fast blue mouse",new ProductCategory(),1200L);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            ProductRating productRating = ProductRating.builder().product(product).dateComment(date).opinion("Awesome opinion").title("Title").username("duke").rating(4).username("duke").build();

            when(authenticatedUser.getUsername()).thenReturn("duke");
            when(ratingConverter.from(ratingCommand,product,"duke",date )).thenReturn(productRating);
            when(ratingRepository.save(ratingCaptor.capture())).thenAnswer(invocation -> {
                ProductRating ratingToSave = invocation.getArgument(0,ProductRating.class);
                ratingToSave.setId(3L);
                return ratingToSave;
            });

            Long result = cut.ratingCreate(ratingCommand, product);
            ProductRating value = ratingCaptor.getValue();

            assertEquals(3L,result);
            assertEquals(value.getProduct(),product);
            assertEquals(4,value.getRating());
            assertEquals("duke",value.getUsername());

        }

        @Test
        @DisplayName("- should raise exception when can not convert provided rating command")
        void test2(){
            CreateRatingCommand createRatingCommand = null;
            Product product = Mockito.mock(Product.class);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            when(authenticatedUser.getUsername()).thenReturn("duke");
            when(ratingConverter.from(createRatingCommand,product,"duke",date)).thenThrow(IllegalArgumentException.class);

            assertThatThrownBy(() -> cut.ratingCreate(createRatingCommand,product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNoCause();
            verifyNoInteractions(ratingRepository);
        }

    }


    }