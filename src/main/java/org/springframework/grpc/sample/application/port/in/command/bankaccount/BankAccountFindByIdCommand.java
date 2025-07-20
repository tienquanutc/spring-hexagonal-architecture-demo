package org.springframework.grpc.sample.application.port.in.command.bankaccount;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BankAccountFindByIdCommand {
    private String id;
}
