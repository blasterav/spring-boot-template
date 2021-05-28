package com.volans.template.annotations;

import com.volans.template.exception.InvalidRequestException;
import com.volans.template.model.StatusConstants;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class MinAnnotation implements ConstraintValidator<Min, Object> {

    private StatusConstants.HttpConstants exception;
    private long min;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.exception = constraintAnnotation.exception();
        this.min = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null) {
            if (value instanceof Integer) {
                if ((Integer) value < min) {
                    throw new InvalidRequestException(exception);
                }
            } else if (value instanceof Long) {
                if ((Long) value < min) {
                    throw new InvalidRequestException(exception);
                }
            }

        }
        return true;
    }

}
