package org.springframework.grpc.sample.application.validator.base;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequestValidator implements ConstraintValidator<RequestValidation, Object> {

    private BaseValidator<Object> validator;

    @Override
    public void initialize(RequestValidation annotation) {
        try {
            this.validator = (BaseValidator<Object>) annotation.using().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create validator", e);
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return validator.isValid(value, context);
    }
}