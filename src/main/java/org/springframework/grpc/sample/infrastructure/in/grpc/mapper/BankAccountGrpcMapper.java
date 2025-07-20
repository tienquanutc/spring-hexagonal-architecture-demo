package org.springframework.grpc.sample.infrastructure.in.grpc.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.grpc.account.proto.*;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BankAccountGrpcMapper {

    default BankAccountResult toBankAccountResult(BankAccountMessage message) {
        return new BankAccountResult()
            .setId(message.getId())
            .setEmail(message.getEmail())
            .setFirstName(message.getFirstName())
            .setLastName(message.getLastName())
            .setAddress(message.getAddress())
            .setPhone(message.getPhone())
            .setCurrency(message.getCurrency())
            .setBalance(BigDecimal.valueOf(message.getBalance()));
    }

    default BankAccountCreateCommand toBankAccountCreateCommand(CreateBankAccountRequest request) {
        return new BankAccountCreateCommand()
            .setEmail(request.getEmail())
            .setFirstName(request.getFirstName())
            .setLastName(request.getLastName())
            .setAddress(request.getAddress())
            .setPhone(request.getPhone())
            .setCurrency(request.getCurrency())
            .setBalance(BigDecimal.valueOf(request.getBalance()));
    }

    default CreateBankAccountResponse toCreateBankAccountResponse(BankAccountResult bankAccountResult) {
        return CreateBankAccountResponse.newBuilder()
            .setBankAccount(toBankAccountMessage(bankAccountResult))
            .build();
    }

    default GetBankAccountByIdResponse toGetBankAccountByIdResponse(BankAccountResult bankAccountResult) {
        return GetBankAccountByIdResponse.newBuilder()
            .setBankAccount(toBankAccountMessage(bankAccountResult))
            .build();
    }

    default BankAccountMessage toBankAccountMessage(BankAccountResult bankAccountResult) {
        return BankAccountMessage.newBuilder()
            .setId(bankAccountResult.getId())
            .setEmail(StringUtils.defaultString(bankAccountResult.getEmail()))
            .setFirstName(StringUtils.defaultString(bankAccountResult.getFirstName()))
            .setLastName(StringUtils.defaultString(bankAccountResult.getLastName()))
            .setAddress(StringUtils.defaultString(bankAccountResult.getAddress()))
            .setPhone(StringUtils.defaultString(bankAccountResult.getPhone()))
            .setCurrency(StringUtils.defaultString(bankAccountResult.getCurrency()))
            .setBalance(Optional.ofNullable(bankAccountResult.getBalance()).map(BigDecimal::doubleValue).orElse(0d))
            .build();
    }

    default DepositBalanceResponse toDepositBalanceResponse(BankAccountResult bankAccountResult) {
        return DepositBalanceResponse.newBuilder()
            .setBankAccount(toBankAccountMessage(bankAccountResult))
            .build();
    }

    default WithdrawBalanceResponse toWithdrawBalanceResponse(BankAccountResult bankAccountResult) {
        return WithdrawBalanceResponse.newBuilder()
            .setBankAccount(toBankAccountMessage(bankAccountResult))
            .build();
    }
}
