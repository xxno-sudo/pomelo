package com.cuckoo.pomelo.common.dao.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    
    private final DynamicDataSourceManager dataSourceManager = DynamicDataSourceManager.getInstance();
    
    @Override
    protected Object determineCurrentLookupKey() {
        String currentDataSourceName = dataSourceManager.getCurrentDataSourceName();
        log.debug("当前使用数据源: {}", currentDataSourceName);
        return currentDataSourceName;
    }
    
    @Override
    protected DataSource determineTargetDataSource() {
        // 动态获取数据源，确保能获取到最新添加的数据源
        String lookupKey = (String) determineCurrentLookupKey();
        if (lookupKey != null) {
            DataSource dataSource = dataSourceManager.getDataSource(lookupKey);
            if (dataSource != null) {
                log.debug("找到数据源: {}", lookupKey);
                return dataSource;
            }
        }
        
        // 如果找不到指定数据源，使用默认数据源
        DataSource defaultDataSource = dataSourceManager.getCurrentDataSource();
        if (defaultDataSource != null) {
            log.debug("使用默认数据源");
            return defaultDataSource;
        }
        
        // 最后回退到父类的实现
        log.debug("回退到父类数据源实现");
        return super.determineTargetDataSource();
    }
    
    /**
     * 刷新数据源映射
     */
    public void refreshDataSources() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        
        // 获取所有数据源
        Map<String, DataSource> allDataSources = dataSourceManager.getAllDataSources();
        for (Map.Entry<String, DataSource> entry : allDataSources.entrySet()) {
            targetDataSources.put(entry.getKey(), entry.getValue());
        }
        
        // 更新数据源映射
        setTargetDataSources(targetDataSources);
        
        // 设置默认数据源
        DataSource currentDataSource = dataSourceManager.getCurrentDataSource();
        if (currentDataSource != null) {
            setDefaultTargetDataSource(currentDataSource);
        }
        
        // 重新初始化
        afterPropertiesSet();
        
        log.info("数据源映射已刷新，共 {} 个数据源", targetDataSources.size());
    }
}