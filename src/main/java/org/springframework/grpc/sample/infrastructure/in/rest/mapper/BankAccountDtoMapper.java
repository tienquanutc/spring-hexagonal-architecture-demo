package org.springframework.grpc.sample.infrastructure.in.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountFindByIdCommand;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.infrastructure.in.rest.request.BankAccountCreateDto;
import org.springframework.grpc.sample.infrastructure.in.rest.request.BankAccountFindByIdDto;
import org.springframework.grpc.sample.infrastructure.in.rest.response.BankAccountDto;

@Mapper(componentModel = "spring")
public interface BankAccountDtoMapper {

    BankAccountCreateCommand toBankAccountCreateCommand(BankAccountCreateDto dto);

    BankAccountFindByIdCommand toBankAccountFindByIdCommand(BankAccountFindByIdDto dto);

    BankAccountDto toBankAccountDto(BankAccountResult result);

}
