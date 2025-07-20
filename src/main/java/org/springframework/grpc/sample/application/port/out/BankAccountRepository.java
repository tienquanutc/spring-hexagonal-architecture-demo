package org.springframework.grpc.sample.application.port.out;

import org.springframework.grpc.sample.domain.model.BankAccount;

import java.math.BigDecimal;
import java.util.Optional;

public interface BankAccountRepository {

    Optional<BankAccount> findByIdWithOptimisticLock(String id);

    Optional<BankAccount> findById(String id);

    BankAccount save(BankAccount bankAccount);

    int depositBalance(String id, BigDecimal amount);

    int withDrawBalance(String id, BigDecimal amount);

}
