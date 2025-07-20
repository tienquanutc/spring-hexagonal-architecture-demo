package org.springframework.grpc.sample.shared.grpc;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GrpcResult<T> extends Result<T> {

    public GrpcResult(T result, Throwable throwable) {
        super(result, throwable);
    }

    public static <T> GrpcResult<T> success(T result) {
        return new GrpcResult<>(result, null);
    }

    public static <T> GrpcResult<T> fail(Throwable throwable) {
        return new GrpcResult<>(null, throwable);
    }


    public <M extends Message> Optional<M> getFirstErrorDetail(Class<M> type) {
        List<M> allErrorDetails = getAllErrorDetails(type);
        if (allErrorDetails == null || allErrorDetails.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(allErrorDetails.getFirst());
    }

    public <M extends Message> List<M> getAllErrorDetails(Class<M> type) {
        return getAllErrorDetails().stream()
            .map(any -> unpack(any, type))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    public Optional<Any> getFirstErrorDetail() {
        List<Any> allErrorDetails = getAllErrorDetails();
        if (allErrorDetails == null || allErrorDetails.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(allErrorDetails.getFirst());
    }

    public List<Any> getAllErrorDetails() {
        if (throwable == null) {
            return Collections.emptyList();
        }
        Status status = StatusProto.fromThrowable(throwable);
        if (status == null) return Collections.emptyList();
        return status.getDetailsList();
    }

    private <M extends Message> Optional<M> unpack(Any any, Class<M> type) {
        try {
            return Optional.of(any.unpack(type));
        } catch (InvalidProtocolBufferException e) {
            log.debug("Failed to unpack [{}]: {}", type.getSimpleName(), e.getMessage());
            return Optional.empty();
        }
    }
}
