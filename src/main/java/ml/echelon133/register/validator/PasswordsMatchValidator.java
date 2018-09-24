package ml.echelon133.register.validator;

import ml.echelon133.register.NewUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, NewUserDto> {

    @Override
    public boolean isValid(NewUserDto newUserDto, ConstraintValidatorContext constraintValidatorContext) {
        Boolean isValid = false;
        try {
            isValid = newUserDto.getPassword().equals(newUserDto.getPassword2());
        } catch (NullPointerException ex) {
            // skip, because isValid is still false
        }
        return isValid;
    }

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
    }
}