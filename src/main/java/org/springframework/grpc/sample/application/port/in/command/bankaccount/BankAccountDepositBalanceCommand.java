package org.springframework.grpc.sample.application.port.in.command.bankaccount;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class BankAccountDepositBalanceCommand {
    private String id;
    private BigDecimal amount;
}
