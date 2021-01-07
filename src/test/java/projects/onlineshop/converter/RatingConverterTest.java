package projects.onlineshop.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projects.helpers.DataHelper;
import projects.onlineshop.domain.model.Product;
import projects.onlineshop.domain.model.ProductCategory;
import projects.onlineshop.domain.model.ProductRating;
import projects.onlineshop.web.command.CreateRatingCommand;
import projects.onlineshop.web.command.RegisterUserCommand;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rating Converter Specification")
class RatingConverterTest {

    RatingConverter cut;

    @BeforeEach
    void setUp(){
        cut = new RatingConverter();
    }

    @Test
    @DisplayName("- should convert valid creation command to product rating ")
    void shouldConvertValidCreationCommandToProductRating () {

        String username = "username";
        String date = "2020-01-01";
        CreateRatingCommand command = DataHelper.ratingCommand(5,"Produkt jest super","Ocena");

        Product product = DataHelper.product("Intel","Super nowy procesor", new ProductCategory(),2000L);

        ProductRating expected = DataHelper.productRating(5,"Ocena","Produkt jest super",date, product,username);

        ProductRating actual = cut.from(command,product,username,date);


        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("- should raise exception when converting from null")
    void shouldRaiseExceptionWhenConvertingFromNull() {
        CreateRatingCommand ratingCommand = null;
        String username = "username";
        String date = "2020-01-01";
        Product product = DataHelper.product("Intel","Super nowy procesor", new ProductCategory(),2000L);

        assertThrows(IllegalArgumentException.class, () -> cut.from(ratingCommand,product,username,date));
    }
}