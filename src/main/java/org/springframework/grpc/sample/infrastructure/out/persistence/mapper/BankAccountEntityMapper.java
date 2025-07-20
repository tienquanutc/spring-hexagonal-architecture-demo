package org.springframework.grpc.sample.infrastructure.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.springframework.grpc.sample.domain.model.BankAccount;
import org.springframework.grpc.sample.infrastructure.out.persistence.entity.BankAccountEntity;

@Mapper(componentModel = "spring")
public interface BankAccountEntityMapper {

    BankAccount toDomain(BankAccountEntity bankAccountEntity);

    BankAccountEntity toEntity(BankAccount bankAccount);

}
