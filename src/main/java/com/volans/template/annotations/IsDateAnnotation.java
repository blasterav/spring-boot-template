package com.volans.template.annotations;

import com.volans.template.exception.InvalidRequestException;
import com.volans.template.model.StatusConstants;
import com.volans.template.util.DateTimeUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class IsDateAnnotation implements ConstraintValidator<IsDate, String> {

    private StatusConstants.HttpConstants exception;

    private String format;

    @Override
    public void initialize(IsDate constraintAnnotation) {
        this.exception = constraintAnnotation.exception();
        this.format = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (date != null) {
            if (DateTimeUtils.parse(date, format).isPresent()) {
                return true;
            }
            throw new InvalidRequestException(exception);
        }
        return true;
    }

}
