package com.cuckoo.pomelo.facade.request;

import com.cuckoo.pomelo.facade.bean.BaseQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRequest extends BaseQuery {

    protected String tenantId;
}
