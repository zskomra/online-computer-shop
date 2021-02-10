package projects.onlineshop.converter;

import projects.onlineshop.exception.UnconvertibleDataException;

public class ConverterUtils {
    public static void requiredNotNull(Object value) {
        requiredNotNull(value, "Cannot convert from null");
    }

    public static void requiredNotNull(Object value, String message) {
        if (value == null) {
            throw new UnconvertibleDataException(message);
        }
    }

}
