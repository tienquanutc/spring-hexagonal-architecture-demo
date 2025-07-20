package org.springframework.grpc.sample.application.port.out;

import org.springframework.grpc.sample.domain.model.BankAccount;

import java.math.BigDecimal;

public interface BankAccountEventPublisher {

    void publishBankAccountCreated(BankAccount bankAccount);

    void publishBankAccountDeposited(BankAccount bankAccount, BigDecimal amount);

    void publishBankAccountWithdrew(BankAccount bankAccount, BigDecimal amount);

    void publishBankAccountUpdated(BankAccount bankAccount);

    void publishBankAccountDeleted(BankAccount bankAccount);
}
