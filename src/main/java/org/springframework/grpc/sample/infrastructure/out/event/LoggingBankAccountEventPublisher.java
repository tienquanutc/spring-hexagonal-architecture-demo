package org.springframework.grpc.sample.infrastructure.out.event;

import org.springframework.grpc.sample.application.port.out.BankAccountEventPublisher;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoggingBankAccountEventPublisher implements BankAccountEventPublisher {

    @Override
    public void publishBankAccountCreated(BankAccount bankAccount) {

    }

    @Override
    public void publishBankAccountDeposited(BankAccount bankAccount, BigDecimal amount) {

    }

    @Override
    public void publishBankAccountWithdrew(BankAccount bankAccount, BigDecimal amount) {

    }

    @Override
    public void publishBankAccountUpdated(BankAccount bankAccount) {

    }

    @Override
    public void publishBankAccountDeleted(BankAccount bankAccount) {

    }
}
