package ml.echelon133.exception;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class FailedFieldValidationException extends Exception {
    private List<FieldError> fieldErrors;

    public FailedFieldValidationException(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public List<String> getTextErrors() {
        List<String> textErrors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            textErrors.add(error.getField() + " " + error.getDefaultMessage());
        }
        return textErrors;
    }
}
