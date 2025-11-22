package com.cuckoo.pomelo.facade.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Query implements Serializable {

    protected final static String ASC = "asc";
    protected final static String DESC = "desc";
    private static final long serialVersionUID = -25777128784350903L;
    protected String fields;
    protected List<OrderField> orderFields;


    public Query orderByField(String fieldName, boolean isAsc) {
        Optional.ofNullable(orderFields).orElseGet(ArrayList::new).add(new OrderField(fieldName, isAsc ? ASC : DESC));
        return this;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    static class OrderField implements Serializable {

        private static final long serialVersionUID = -8777018296827472718L;
        private String fieldName;
        private String order;
    }
}
