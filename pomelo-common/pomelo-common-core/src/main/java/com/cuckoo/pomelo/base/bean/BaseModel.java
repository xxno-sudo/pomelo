package com.cuckoo.pomelo.base.bean;

import com.cuckoo.pomelo.SensitiveReflectionToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Setter
@Getter
public class BaseModel implements Serializable {


    @Override
    public String toString() {
        return new SensitiveReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE).toString();
    }
}
