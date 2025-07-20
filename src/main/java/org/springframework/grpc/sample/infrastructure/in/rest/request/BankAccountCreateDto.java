package org.springframework.grpc.sample.infrastructure.in.rest.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class BankAccountCreateDto {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String currency;
    private BigDecimal balance;
}
