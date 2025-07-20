package org.springframework.grpc.sample.application.service;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.grpc.sample.application.port.in.BankAccountUseCase;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountDepositBalanceCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountFindByIdCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountWithdrawBalanceCommand;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.application.port.out.BankAccountCachePort;
import org.springframework.grpc.sample.application.port.out.BankAccountEventPublisher;
import org.springframework.grpc.sample.application.port.out.BankAccountRepository;
import org.springframework.grpc.sample.application.service.mapper.BankAccountCommandMapper;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountUseCase {

    private final BankAccountCommandMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountCachePort bankAccountCachePort;
    private final BankAccountEventPublisher bankAccountEventPublisher;

    @Override
    public BankAccountResult createBankAccount(BankAccountCreateCommand command) {
        BankAccount bankAccount = bankAccountMapper.toBankAccountEntity(command);
        this.bankAccountRepository.save(bankAccount);
        // Publish the event after saving the bank account
        bankAccountEventPublisher.publishBankAccountCreated(bankAccount);
        // Convert the BankAccount entity to BankAccountResult
        return bankAccountMapper.toBankAccountResult(bankAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankAccountResult> getBankAccountById(BankAccountFindByIdCommand command) {
        String accountId = command.getId();
        return this.bankAccountCachePort.getBankAccountById(accountId).or(() -> {
            Optional<BankAccount> optional = this.bankAccountRepository.findById(accountId);
            optional.ifPresent(this.bankAccountCachePort::putBankAccount);
            return optional;
        }).map(bankAccountMapper::toBankAccountResult);
    }

    @Override
    @Transactional
    public void depositBankAccount(BankAccountDepositBalanceCommand command) {
        String accountId = command.getId();
        BigDecimal depositAmount = command.getAmount();

        // Validate the deposit amount
        if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than or equal to zero.");
        }

        // Fetch the bank account by ID
        BankAccount bankAccount = this.bankAccountRepository.findByIdWithOptimisticLock(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account with ID " + accountId + " does not exist."));
        // Check if the amount is valid
        if (bankAccount.getBalance().compareTo(command.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        // Update the balance
        int updated = this.bankAccountRepository.depositBalance(accountId, depositAmount);
        if (updated != 1) {
            throw new IllegalStateException("Failed to deposit balance for account ID: " + command.getId());
        }
        this.bankAccountCachePort.evictBankAccount(accountId);
        this.bankAccountEventPublisher.publishBankAccountDeposited(bankAccount, depositAmount);
    }

    @Override
    @Transactional
    public void withdrawBankAccount(BankAccountWithdrawBalanceCommand command) {
        String accountId = command.getId();

        // Validate the withdrawal amount
        BigDecimal withDrawAmount = command.getAmount();
        if (withDrawAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than or equal to zero.");
        }
        // Fetch the bank account by ID
        BankAccount bankAccount = this.bankAccountRepository.findByIdWithOptimisticLock(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account with ID " + accountId + " does not exist."));
        // Update the balance
        int updated = this.bankAccountRepository.withDrawBalance(command.getId(), command.getAmount());
        if (updated != 1) {
            throw new IllegalStateException("Failed to withdraw balance for account ID: " + command.getId());
        }
        this.bankAccountCachePort.evictBankAccount(accountId);
        this.bankAccountEventPublisher.publishBankAccountWithdrew(bankAccount, withDrawAmount);
    }
}
