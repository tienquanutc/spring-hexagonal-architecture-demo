package org.springframework.grpc.sample.infrastructure.in.rest.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BankAccountFindByIdDto {
    private String id;
}
