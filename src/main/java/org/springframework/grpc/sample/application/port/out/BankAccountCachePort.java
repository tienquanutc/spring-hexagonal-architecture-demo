package org.springframework.grpc.sample.application.port.out;

import org.springframework.grpc.sample.domain.model.BankAccount;

import java.util.Optional;

public interface BankAccountCachePort {
    Optional<BankAccount> getBankAccountById(String id);

    void putBankAccount(BankAccount account);

    void evictBankAccount(String id);
}
