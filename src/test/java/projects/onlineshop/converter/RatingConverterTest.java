package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.web.command.CreateRatingCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rating Converter Specification")
class RatingConverterTest {

    RatingConverter cut;

    @BeforeEach
    void setUp() {
        cut = new RatingConverter();
    }


    @DisplayName("1. Converting from Create Product Rating Command")
    @Nested
    class ConvertingFromCreateProductRatingCommand {


        @Test
        @DisplayName("- should convert valid creation rating command to product rating")
        void shouldConvertValidCreationRatingCommandToProductRating() {
            CreateRatingCommand command = DataHelper.ratingCommand(4,"Some awesome opinion","Title");
            Product product = DataHelper.product(2L,"Graphics Card","New and blue graphic card",new ProductCategory(),200L);

            ProductRating productRating = cut.from(command,product,"user@user.pl","2021-01-01");
            ProductRating expected = DataHelper.productRating(4,"Title","Some awesome opinion","2021-01-01",product,"user@user.pl");

            assertEquals(expected,productRating);

        }

        @Test
        @DisplayName("- should raise exception when convert from null")
        void shouldRaiseExceptionWhenConvertFromNull() {
            Product product = DataHelper.product(2L,"Graphics Card","New and blue graphic card",new ProductCategory(),200L);

            assertThatThrownBy(() -> cut.from(null,product,"user@user.pl","2021-01-01"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNoCause()
                    .hasMessageContaining("Create rating command cannot be null");
        }

    }
}


