package com.warehouse.department.domain.helper;

import java.util.Objects;
import java.util.function.Consumer;

public class Result<S, E> {

    private final boolean isSuccess;

    private final S success;
    private final E failure;

    private Result(final boolean isSuccess, final S success, final E failure) {
        if (isSuccess && Objects.nonNull(failure)) {
            throw new IllegalStateException("Process that succeeded cannot have failure report");
        } else if (!isSuccess && Objects.nonNull(success)) {
            throw new IllegalStateException("Process that failed cannot have success value");
        }

        this.success = success;
        this.failure = failure;
        this.isSuccess = isSuccess;
    }

    public static <S, E> Result<S, E> success(final S successObj) {
        return new Result<>(true, successObj, null);
    }

    public static <S, E> Result<S, E> failure(final E failureObj) {
        return new Result<>(false, null, failureObj);
    }

    public static <S, E> Result<S, E> success() {
        return new Result<>(true, null, null);
    }

    public static <S, E> Result<S, E> failure() {
        return new Result<>(false, null, null);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public S getSuccess() {
        if (!isSuccess) {
            throw new UnsupportedOperationException("Cannot obtain success object when result is failure");
        }

        return success;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public E getFailure() {
        if (isSuccess) {
            throw new UnsupportedOperationException("Cannot obtain failure object when result is success");
        }

        return failure;
    }

    public void ifSuccess(final Consumer<S> successConsumer){
        if(isSuccess){
            successConsumer.accept(getSuccess());
        }
    }

    public void ifFailure(final Consumer<E> failureConsumer){
        if(!isSuccess){
            failureConsumer.accept(getFailure());
        }
    }

    public void orElse(final Consumer<S> successConsumer, final Consumer<E> failureConsumer) {
        if (isSuccess) {
            successConsumer.accept(getSuccess());
        } else {
            failureConsumer.accept(getFailure());
        }
    }

}
