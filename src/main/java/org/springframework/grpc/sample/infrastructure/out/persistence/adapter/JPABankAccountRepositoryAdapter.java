package org.springframework.grpc.sample.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.grpc.sample.application.port.out.BankAccountRepository;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.grpc.sample.infrastructure.out.persistence.entity.BankAccountEntity;
import org.springframework.grpc.sample.infrastructure.out.persistence.mapper.BankAccountEntityMapper;
import org.springframework.grpc.sample.infrastructure.out.persistence.repository.SpringDataBankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JPABankAccountRepositoryAdapter implements BankAccountRepository {

    private final BankAccountEntityMapper mapper;
    private final SpringDataBankAccountRepository repository;

    @Override
    public Optional<BankAccount> findByIdWithOptimisticLock(String id) {
        return this.repository.findByIdWithOptimisticLock(id).map(mapper::toDomain);
    }

    @Override
    public Optional<BankAccount> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccountEntity entity = mapper.toEntity(bankAccount);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public int depositBalance(String id, BigDecimal amount) {
        return this.repository.depositBalance(id, amount);
    }

    @Override
    public int withDrawBalance(String id, BigDecimal amount) {
        return this.repository.withDrawBalance(id, amount);
    }

}
