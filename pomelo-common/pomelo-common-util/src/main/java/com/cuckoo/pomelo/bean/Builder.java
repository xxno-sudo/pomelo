package com.cuckoo.pomelo.bean;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Builder<T> {
    private final Supplier<T> constructor;

    private Consumer<T> consumer;

    private Builder(Supplier<T> constructor) {
        this.constructor = constructor;
    }

    public static <T> Builder<T> of(Supplier<T> constructor) {
        return new Builder<>(constructor);
    }

    public <P> Builder<T> with(BiConsumer<T, P> consumer, P p) {
        Consumer<T> c = instance -> consumer.accept(instance, p);
        if (this.consumer == null) {
            this.consumer = c;
        } else {
            this.consumer = this.consumer.andThen(c);
        }
        return this;
    }

    public T build() {
        T value = constructor.get();
        Optional.ofNullable(this.consumer).ifPresent(a -> a.accept(value));
        return value;
    }

}