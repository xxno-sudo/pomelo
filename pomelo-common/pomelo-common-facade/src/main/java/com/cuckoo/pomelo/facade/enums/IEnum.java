package com.cuckoo.pomelo.facade.enums;

import java.util.EnumSet;
import java.util.Objects;

public interface IEnum<T> {
    static <E extends Enum<E> & IEnum<T>, T> E of(Class<E> clazz, T value) {
        EnumSet<E> all = EnumSet.allOf(clazz);
        return all.stream().filter(e -> e.eq(value)).findFirst().orElse(null);
    }

    static <E extends Enum<E> & IEnum<?>> E ofName(Class<E> clazz, String name) {
        EnumSet<E> all = EnumSet.allOf(clazz);
        return all.stream().filter(e -> Objects.equals(e.getName(), name)).findFirst().orElse(null);
    }

    T getCode();

    String getName();

    /**
     * 枚举有特殊判断方法的时候重写此方法。
     *
     */
    default boolean eq(T value) {
        return Objects.equals(this.getCode(), value);
    }

}
