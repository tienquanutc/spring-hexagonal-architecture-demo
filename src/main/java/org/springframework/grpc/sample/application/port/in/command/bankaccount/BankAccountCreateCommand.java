package org.springframework.grpc.sample.application.port.in.command.bankaccount;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.grpc.sample.application.validator.CreateAccountDtoValidator;
import org.springframework.grpc.sample.application.validator.base.RequestValidation;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@RequestValidation(using = CreateAccountDtoValidator.class, message = "Invalid account creation request")
public class BankAccountCreateCommand {

    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String currency;
    private BigDecimal balance;
}
