package org.springframework.grpc.sample.application.port.out;

import org.springframework.grpc.sample.application.port.in.command.payment.PaymentChargeCommand;
import org.springframework.grpc.sample.application.port.in.result.payment.PaymentChargeResult;

public interface PaymentPort {
    PaymentChargeResult charge(PaymentChargeCommand command);
}
