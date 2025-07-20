package org.springframework.grpc.sample.infrastructure.in.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.account.proto.*;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountDepositBalanceCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountFindByIdCommand;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountWithdrawBalanceCommand;
import org.springframework.grpc.sample.application.port.in.result.bankaccount.BankAccountResult;
import org.springframework.grpc.sample.application.port.in.command.bankaccount.BankAccountCreateCommand;
import org.springframework.grpc.sample.infrastructure.in.grpc.mapper.BankAccountGrpcMapper;
import org.springframework.grpc.sample.application.port.in.BankAccountUseCase;
import org.springframework.grpc.server.service.GrpcService;

import java.math.BigDecimal;


@Slf4j
@GrpcService
@RequiredArgsConstructor
public class BankAccountGrpcService extends BankAccountServiceGrpc.BankAccountServiceImplBase {

    private final BankAccountGrpcMapper bankAccountMapper;
    private final BankAccountUseCase bankAccountService;

    @Override
    public void createBankAccount(CreateBankAccountRequest request, StreamObserver<CreateBankAccountResponse> responseObserver) {
        BankAccountCreateCommand createAccountDto = bankAccountMapper.toBankAccountCreateCommand(request);
        BankAccountResult bankAccountDto = this.bankAccountService.createBankAccount(createAccountDto);
        CreateBankAccountResponse createBankResponse = bankAccountMapper.toCreateBankAccountResponse(bankAccountDto);
        responseObserver.onNext(createBankResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getBankAccountById(GetBankAccountByIdRequest request, StreamObserver<GetBankAccountByIdResponse> responseObserver) {
        String id = request.getId();
        log.info("Get Bank Account by id: {}", id);
        BankAccountFindByIdCommand findByIdCommand = new BankAccountFindByIdCommand().setId(id);
        this.bankAccountService.getBankAccountById(findByIdCommand).ifPresentOrElse(
            bankAccountResult -> {
                GetBankAccountByIdResponse response = bankAccountMapper.toGetBankAccountByIdResponse(bankAccountResult);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            },
            () -> responseObserver.onError(new RuntimeException("Bank account not found"))
        );
    }

    @Override
    public void depositBalance(DepositBalanceRequest request, StreamObserver<DepositBalanceResponse> responseObserver) {
        this.bankAccountService.depositBankAccount(
            new BankAccountDepositBalanceCommand()
                .setId(request.getId())
                .setAmount(BigDecimal.valueOf(request.getAmount()))
        );
        this.bankAccountService.getBankAccountById(new BankAccountFindByIdCommand().setId(request.getId())).ifPresentOrElse(
            bankAccountResult -> {
                responseObserver.onNext(bankAccountMapper.toDepositBalanceResponse(bankAccountResult));
                responseObserver.onCompleted();
            },
            () -> responseObserver.onError(new RuntimeException("Bank account not found"))
        );
    }

    @Override
    public void withdrawBalance(WithdrawBalanceRequest request, StreamObserver<WithdrawBalanceResponse> responseObserver) {
        this.bankAccountService.withdrawBankAccount(
            new BankAccountWithdrawBalanceCommand()
                .setId(request.getId())
                .setAmount(BigDecimal.valueOf(request.getAmount()))
        );
        this.bankAccountService.getBankAccountById(new BankAccountFindByIdCommand().setId(request.getId())).ifPresentOrElse(
            bankAccountResult -> {
                responseObserver.onNext(bankAccountMapper.toWithdrawBalanceResponse(bankAccountResult));
                responseObserver.onCompleted();
            },
            () -> responseObserver.onError(new RuntimeException("Bank account not found"))
        );
    }

    @Override
    public void getAllByBalance(GetAllByBalanceRequest request, StreamObserver<GetAllByBalanceResponse> responseObserver) {
        super.getAllByBalance(request, responseObserver);
    }

    @Override
    public void getAllByBalanceWithPagination(GetAllByBalanceWithPaginationRequest request, StreamObserver<GetAllByBalanceWithPaginationResponse> responseObserver) {
        super.getAllByBalanceWithPagination(request, responseObserver);
    }
}
