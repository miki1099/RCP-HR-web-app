package pl.uginf.rcphrwebapp.hr;

import java.util.List;

public class ValidationException extends RuntimeException {

    public ValidationException(List<String> errors) {
        super(formatErrors(errors));
    }

    public ValidationException(String error) {
        super(error);
    }

    private static String formatErrors(List<String> errors) {
        return String.join(System.lineSeparator(), errors);
    }
}
