package org.springframework.grpc.sample.application.validator.base;

import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseValidator<T> {

    protected abstract boolean isValid(T value, ConstraintValidatorContext context);

    protected void addViolation(ConstraintValidatorContext context, String field, String message) {
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(field)
            .addConstraintViolation();
    }
}
