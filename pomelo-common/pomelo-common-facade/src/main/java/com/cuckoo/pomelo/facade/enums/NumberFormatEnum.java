package com.cuckoo.pomelo.facade.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public enum NumberFormatEnum implements IEnum<String>{
    DEFAULT("","默认规则","四舍五入,保留8位小数",source->source.setScale(2, RoundingMode.UP)),
    ORIGINAL("","不取整","四舍五入,保留8位小数",source->source.setScale(8, RoundingMode.UP));

    final  String code;
    final String name;

    final String descr;

    final Function<BigDecimal,BigDecimal> func;

    NumberFormatEnum(String code, String name, String descr, Function<BigDecimal, BigDecimal> func) {
        this.code = code;
        this.name = name;
        this.descr = descr;
        this.func = func;
    }

    public BigDecimal format(BigDecimal source){
        return this.func.apply(source);
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
