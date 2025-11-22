package com.cuckoo.pomelo.facade.response;


import com.cuckoo.pomelo.facade.enums.IEnum;

public enum ResponseState implements IEnum<String> {

    SUCCESS("000000", "成功"), ERROR("999999", "失败");

    private final String code;
    private final String name;

    ResponseState(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
