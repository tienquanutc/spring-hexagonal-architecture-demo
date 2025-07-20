package org.springframework.grpc.sample.application.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.grpc.sample.application.validator.base.BaseValidator;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;

public class CreateAccountDtoValidator extends BaseValidator<BankAccountCreateCommand> {

    @Override
    public boolean isValid(BankAccountCreateCommand request, ConstraintValidatorContext context) {
        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (request.getEmail() == null || request.getEmail().length() < 3) {
            context.buildConstraintViolationWithTemplate("Email must be at least 3 characters")
                .addPropertyNode("email")
                .addConstraintViolation();
            valid = false;
        }

        if (request.getPhone() == null || request.getPhone().length() <= 10) {
            context.buildConstraintViolationWithTemplate("Phone must more than 10")
                .addPropertyNode("phone")
                .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
