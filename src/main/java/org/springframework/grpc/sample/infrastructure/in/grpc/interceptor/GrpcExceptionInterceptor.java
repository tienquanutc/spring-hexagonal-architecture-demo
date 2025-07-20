package org.springframework.grpc.sample.infrastructure.in.grpc.interceptor;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import io.grpc.StatusException;
import io.grpc.protobuf.StatusProto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.grpc.error.proto.BIDDErrorInfo;
import org.springframework.grpc.sample.domain.exception.BiddException;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class GrpcExceptionInterceptor implements GrpcExceptionHandler {

    @Override
    public StatusException handleException(Throwable throwable) {
        return switch (throwable) {
            case BiddException ex -> handleBIDDException(ex);
            case MethodArgumentNotValidException ex -> handleMethodArgumentNotValidException(ex);
            case ConstraintViolationException ex -> handleConstraintViolationException(ex);
            default -> handleDefaultException(throwable);
        };
    }

    private StatusException handleBIDDException(BiddException ex) {
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
            .setCode(Code.INTERNAL.getNumber())
            .setMessage(ex.getMessage())
            .addDetails(Any.pack(
                BIDDErrorInfo.newBuilder()
                    .setCode(ex.getCode())
                    .setMessage(ex.getMessage())
                    .setErrorSource(ex.getErrorSource())
                    .addAllStackTrace(List.of(ExceptionUtils.getStackFrames(ex)))
                    .build()
            ))
            .build();
        return StatusProto.toStatusException(status);
    }

    private StatusException handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT.getNumber())
            .setMessage(ex.getMessage())
            .addAllDetails(
                ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(it -> Any.pack(
                            BIDDErrorInfo.newBuilder()
                                .setCode(String.valueOf(Code.INVALID_ARGUMENT))
                                .setMessage(it.getDefaultMessage())
                                .setErrorSource(it.getField())
                                .addAllStackTrace(List.of(ExceptionUtils.getStackFrames(ex)))
                                .build()
                        )
                    ).toList()
            )
            .build();
        return StatusProto.toStatusException(status);
    }

    private StatusException handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT.getNumber())
            .setMessage(ex.getMessage())
            .addAllDetails(violations.stream().map(it -> Any.pack(
                BIDDErrorInfo.newBuilder()
                    .setCode(String.valueOf(Code.INVALID_ARGUMENT))
                    .setMessage(it.getMessage())
                    .setErrorSource(it.getPropertyPath().toString().replaceFirst("^[^.]+\\.arg\\d+\\.", ""))
                    .addAllStackTrace(List.of(ExceptionUtils.getStackFrames(ex)))
                    .build()
            )).toList())
            .build();
        return StatusProto.toStatusException(status);
    }

    private StatusException handleDefaultException(Throwable ex) {
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
            .setCode(Code.INTERNAL.getNumber())
            .setMessage(ex.getMessage())
            .addDetails(Any.pack(
                BIDDErrorInfo.newBuilder()
                    .setCode("001")
                    .setMessage(ex.getMessage())
                    .addAllStackTrace(List.of(ExceptionUtils.getStackFrames(ex)))
                    .build()
            ))
            .build();
        return StatusProto.toStatusException(status);
    }

}
