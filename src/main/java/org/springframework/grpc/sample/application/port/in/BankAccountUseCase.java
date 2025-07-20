package org.springframework.grpc.sample.application.port.in;

import jakarta.validation.Valid;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountDepositBalanceCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountFindByIdCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountWithdrawBalanceCommand;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@Service
public interface BankAccountUseCase {

    BankAccountResult createBankAccount(@Valid BankAccountCreateCommand command);

    Optional<BankAccountResult> getBankAccountById(@Valid BankAccountFindByIdCommand command);

    void depositBankAccount(@Valid BankAccountDepositBalanceCommand command);

    void withdrawBankAccount(@Valid BankAccountWithdrawBalanceCommand command);

}
