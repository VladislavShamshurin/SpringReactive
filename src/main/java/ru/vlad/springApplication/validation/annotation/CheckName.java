package ru.vlad.springApplication.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CheckName {
    String message() default "Name already exists!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
