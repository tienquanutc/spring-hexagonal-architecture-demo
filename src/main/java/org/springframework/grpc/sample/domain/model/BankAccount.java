package org.springframework.grpc.sample.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.grpc.sample.domain.vo.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class BankAccount {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phone;

    private Currency currency;

    private BigDecimal balance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
