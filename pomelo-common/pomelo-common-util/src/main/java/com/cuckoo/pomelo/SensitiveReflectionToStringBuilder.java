package com.cuckoo.pomelo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;

public class SensitiveReflectionToStringBuilder extends ReflectionToStringBuilder {

    public SensitiveReflectionToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    @Override
    protected Object getValue(Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(SensitiveField.class) && field.getType() == String.class) {
            String value = String.class.cast(field.get(this.getObject()));
            if (value == null) {
                return null;
            }
            return field.getAnnotation(SensitiveField.class).value().getMaskFunc().apply(value);
        }
        return super.getValue(field);

    }
}
