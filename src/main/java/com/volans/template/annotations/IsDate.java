package com.volans.template.annotations;

import com.volans.template.util.DateTimeUtils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.volans.template.model.StatusConstants.HttpConstants;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IsDateAnnotation.class })
public @interface IsDate {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    HttpConstants exception() default HttpConstants.BAD_REQUEST;

    String format() default DateTimeUtils.DATETIME_FORMAT_YYYY_MM_DD_T_HH_MM_SS;

}
