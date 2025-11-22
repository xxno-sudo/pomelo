package com.cuckoo.pomelo.facade;

import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class Either<L, R> {

    private final L error;
    private final R ok;

    private Either(L l, R r) {
        this.error = l;
        this.ok = r;
    }

    public static <L, R> Either<L, R> left(L l) {
        return new Either<L, R>(l, null);
    }

    public static <L, R> Either<L, R> right(R r) {
        return new Either<L, R>(null, r);
    }

    public static <L, R> Either<L, List<R>> converse(List<Either<L, R>> eithers, BinaryOperator<L> acc) {
        if (eithers.stream().allMatch(Either::isOk)) {
            return right(eithers.stream().map(Either::getOk).collect(Collectors.toList()));
        }
        return left(eithers.stream().filter(Either::isError).map(Either::getError).reduce(acc).orElseThrow());
    }

    public L getError() {
        return error;
    }

    public R getOk() {
        return ok;
    }

    public boolean isError() {
        return Objects.nonNull(error);
    }

    public boolean isOk() {
        return Objects.nonNull(ok);
    }

    public <T> Either<L, T> map(Function<R, T> func) {
        if (isError()) {
            return left(error);
        }
        return right(func.apply(ok));
    }
}
