package ml.echelon133.register.exception;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class NewUserValidationException extends Exception {

    private List<FieldError> fieldErrors;
    private List<ObjectError> objectErrors;

    public NewUserValidationException(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
        this.fieldErrors = fieldErrors;
        this.objectErrors = objectErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public List<ObjectError> getObjectErrors() {
        return objectErrors;
    }

    public List<String> getTextErrors() {
        List<String> textErrors = new ArrayList<>();
        for (FieldError fError : fieldErrors) {
            textErrors.add(fError.getField() + " " + fError.getDefaultMessage());
        }
        for (ObjectError oError : objectErrors) {
            textErrors.add(oError.getDefaultMessage());
        }
        return textErrors;
    }
}
