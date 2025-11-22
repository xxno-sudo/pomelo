package com.cuckoo.pomelo.common.dao.datasource;

import lombok.Data;

/**
 * 数据源验证结果
 */
@Data
public class DataSourceValidationResult {
    private boolean success;
    private String message;
}