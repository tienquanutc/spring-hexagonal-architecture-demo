package org.springframework.grpc.sample.shared.grpc;

import java.util.function.Supplier;

public class Grpcs {

    public static <T> GrpcResult<T> call(Supplier<T> supplier) {
        try {
            return GrpcResult.success(supplier.get());
        } catch (Throwable any) {
            return GrpcResult.fail(any);
        }
    }

}
