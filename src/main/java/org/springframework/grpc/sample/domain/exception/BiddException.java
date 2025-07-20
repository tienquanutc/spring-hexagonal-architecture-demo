package org.springframework.grpc.sample.domain.exception;

import lombok.Getter;

@Getter
public class BiddException extends RuntimeException {
    private final String code;
    private final String message;
    private final String errorSource;
    private final Object request;

    public BiddException(String code, String message, String errorSource, Object request) {
        this.code = code;
        this.message = message;
        this.errorSource = errorSource;
        this.request = request;
    }
}
