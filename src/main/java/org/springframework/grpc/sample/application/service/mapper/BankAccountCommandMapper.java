package org.springframework.grpc.sample.application.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.grpc.sample.domain.vo.Currency;

import java.time.LocalDateTime;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BankAccountCommandMapper {

    default BankAccount toBankAccountEntity(BankAccountCreateCommand dto) {
        return new BankAccount()
            .setEmail(dto.getEmail())
            .setFirstName(dto.getFirstName())
            .setLastName(dto.getLastName())
            .setAddress(dto.getAddress())
            .setPhone(dto.getPhone())
            .setCurrency(Currency.valueOf(Objects.toString(dto.getCurrency(), Currency.USD.name())))
            .setBalance(dto.getBalance())
            .setCreatedAt(LocalDateTime.now())
            .setUpdatedAt(LocalDateTime.now());
    }

    default BankAccountResult toBankAccountResult(BankAccount entity) {
        return new BankAccountResult()
            .setId(entity.getId().toString())
            .setEmail(entity.getEmail())
            .setFirstName(entity.getFirstName())
            .setLastName(entity.getLastName())
            .setAddress(entity.getAddress())
            .setPhone(entity.getPhone())
            .setCurrency(entity.getCurrency().name())
            .setBalance(entity.getBalance());
    }
}
