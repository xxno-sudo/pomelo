package com.cuckoo.pomelo.core.util;

import com.cuckoo.pomelo.facade.enums.IEnum;

import java.util.Objects;

public class EnvUtil {

    public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";


    public static String getEnv() {
        return System.getProperty(SPRING_PROFILES_ACTIVE, EnvEnum.LOCAL.getCode());
    }

    public static boolean isLocal() {
        String property = System.getProperty(SPRING_PROFILES_ACTIVE, EnvEnum.LOCAL.getCode());
        return EnvEnum.active(EnvEnum.LOCAL, property);
    }

    enum EnvEnum implements IEnum<String> {
        DEV("dev", "开发环境"),
        LOCAL("local", "本地环境"),

        UAT("uat", "uat环境"),

        STG("stg", "stg环境"),

        PRD("prd", "生产环境"),

        GRAY("gray", "灰度环境");

        private String code;
        private String name;

        EnvEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static boolean active(EnvEnum target, String code) {
            Objects.nonNull(target);
            return Objects.equals(target.code, code);
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
