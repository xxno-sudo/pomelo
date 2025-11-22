package com.cuckoo.pomelo.facade.enums;


public enum DeletedStatusEnum implements IEnum<Long> {

    UN_DELETED(0L, "未删除"),
    DELETED(1L, "已删除");

    private final Long code;
    private final String name;

    DeletedStatusEnum(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean eq(Long value) {
        if (null == value) {
            return false;
        }
        return this.code == 0 ? value == 0L : value > 0L;
    }

    public Long getCode() {
        if (this.code == 1) {
            return System.currentTimeMillis();
        }
        return code;
    }

}
