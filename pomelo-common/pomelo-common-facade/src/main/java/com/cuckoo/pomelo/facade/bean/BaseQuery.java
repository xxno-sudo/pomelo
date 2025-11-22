package com.cuckoo.pomelo.facade.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseQuery extends Query {
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 2000;
    private static final long serialVersionUID = 7670303645955167554L;
    protected int pageSize = DEFAULT_SIZE;

    protected int page = 1;

    protected int startRow;

    protected int endRow;
}
