package com.cuckoo.pomelo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {

    SensitiveType value() default SensitiveType.COMMON;

    enum SensitiveType {

        COMMON(s -> "****"),
        ID_CARD(s -> {
            if (s.length() == 18) {
                return s.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            } else if (s.length() == 15) {
                return s.replaceAll("(\\d{4})(\\d{8})(\\d{4})", "$1********$3");
            } else {
                return "****";
            }
        }),
        BANK_CARD(s -> "****"),
        EMAIL(s -> s.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2")),
        MOBILE(s -> s.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2")),
        NAME(s -> s.replaceAll("(\\w)", "$1**")),
        ADDRESS(s -> "****");

        private final Function<String, String> maskFunc;


        SensitiveType(Function<String, String> maskFunc) {
            this.maskFunc = maskFunc;
        }

        public Function<String, String> getMaskFunc() {
            return maskFunc;
        }
    }



}
