package org.springframework.grpc.sample.infrastructure.out.persistence.adapter;

import org.springframework.grpc.sample.application.port.in.command.payment.PaymentChargeCommand;
import org.springframework.grpc.sample.application.port.in.result.payment.PaymentChargeResult;
import org.springframework.grpc.sample.application.port.out.PaymentPort;
import org.springframework.stereotype.Component;

@Component
public class RestPaymentPortAdapter implements PaymentPort {
    @Override
    public PaymentChargeResult charge(PaymentChargeCommand command) {
        return null;
    }
}
