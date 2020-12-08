package projects.onlineshop.exception;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductCategoryAlreadyExistsException extends RuntimeException{
    public ProductCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
