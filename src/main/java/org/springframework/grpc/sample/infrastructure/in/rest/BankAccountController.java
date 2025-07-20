package org.springframework.grpc.sample.infrastructure.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.grpc.sample.application.port.in.BankAccountUseCase;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountFindByIdCommand;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.infrastructure.in.rest.mapper.BankAccountDtoMapper;
import org.springframework.grpc.sample.infrastructure.in.rest.request.BankAccountCreateDto;
import org.springframework.grpc.sample.infrastructure.in.rest.request.BankAccountFindByIdDto;
import org.springframework.grpc.sample.infrastructure.in.rest.response.BankAccountDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountDtoMapper bankAccountDtoMapper;
    private BankAccountUseCase bankAccountUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDto> findById(@ModelAttribute BankAccountFindByIdDto requestDto) {
        BankAccountFindByIdCommand command = bankAccountDtoMapper.toBankAccountFindByIdCommand(requestDto);

        return bankAccountUseCase.getBankAccountById(command)
            .map(it -> ResponseEntity.ok(bankAccountDtoMapper.toBankAccountDto(it)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody BankAccountCreateDto requestDto) {
        BankAccountCreateCommand command = bankAccountDtoMapper.toBankAccountCreateCommand(requestDto);
        BankAccountResult result = bankAccountUseCase.createBankAccount(command);
        BankAccountDto responseDto = bankAccountDtoMapper.toBankAccountDto(result);
        return ResponseEntity.ok(responseDto);
    }

}
