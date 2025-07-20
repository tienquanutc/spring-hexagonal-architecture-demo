package org.springframework.grpc.sample.infrastructure.out.persistence.repository;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.grpc.sample.infrastructure.out.persistence.entity.BankAccountEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface SpringDataBankAccountRepository extends CrudRepository<BankAccountEntity, String> {

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT B FROM BankAccountEntity B WHERE B.id = :id")
    Optional<BankAccountEntity> findByIdWithOptimisticLock(String id);

    @Transactional
    @Modifying
    @Query("UPDATE BankAccountEntity B SET B.balance = B.balance + :amount WHERE B.id = :id")
    int depositBalance(String id, BigDecimal amount);


    @Transactional
    @Modifying
    @Query("UPDATE BankAccountEntity B SET B.balance = B.balance - :amount WHERE B.id = :id")
    int withDrawBalance(String id, BigDecimal amount);
}
