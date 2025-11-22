package com.cuckoo.pomelo.facade.enums;


public enum EffStatusEnum implements IEnum<String> {

    EFFECTIVE("A", "有效"), UN_EFFECTIVE("I", "无效");

    private final String code;
    private final String name;

    EffStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
