package org.springframework.grpc.sample.shared.grpc;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Result<T> {

    protected final T value;
    protected final Throwable throwable;

    public Result(T value, Throwable throwable) {
        this.value = value;
        this.throwable = throwable;
    }

    public static <T> Result<T> success(T result) {
        return new Result<>(result, null);
    }

    public static <T> Result<T> fail(Throwable throwable) {
        return new Result<>(null, throwable);
    }

    public boolean isSuccess() {
        return throwable == null;
    }

    public T get() {
        if (isSuccess()) return value;
        throw new RuntimeException("gRPC call failed", throwable);
    }

    public <R> Result<R> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return new Result<>(mapper.apply(value), null);
        } else {
            return new Result<>(null, throwable);
        }
    }

    public <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value);
        } else {
            return new Result<>(null, throwable);
        }
    }

    public Result<T> mapError(Function<Throwable, Throwable> mapper) {
        if (isSuccess()) return this;
        return new Result<>(null, mapper.apply(throwable));
    }

    public T orElse(T fallback) {
        return isSuccess() ? value : fallback;
    }

    public T orElseGet(Function<Throwable, T> fallbackSupplier) {
        return isSuccess() ? value : fallbackSupplier.apply(throwable);
    }

    public T orElseThrow(Function<Throwable, RuntimeException> exceptionMapper) {
        if (isSuccess()) return value;
        throw exceptionMapper.apply(throwable);
    }

    public Optional<T> toOptional() {
        return isSuccess() ? Optional.ofNullable(value) : Optional.empty();
    }

    public Optional<Throwable> getError() {
        return Optional.ofNullable(throwable);
    }

    public void ifSuccess(Consumer<T> consumer) {
        if (isSuccess()) consumer.accept(value);
    }

    public void ifError(Consumer<Throwable> consumer) {
        if (!isSuccess()) consumer.accept(throwable);
    }

    public void ifPresentOrElse(Consumer<? super T> action, Consumer<Throwable> consumer) {
        if (throwable == null) {
            action.accept(value);
        } else {
            consumer.accept(throwable);
        }
    }
}
