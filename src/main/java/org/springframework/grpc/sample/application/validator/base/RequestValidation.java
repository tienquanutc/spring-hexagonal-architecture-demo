package org.springframework.grpc.sample.application.validator.base;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequestValidator.class)
public @interface RequestValidation {
    String message() default "Invalid request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends BaseValidator<?>> using();
}
