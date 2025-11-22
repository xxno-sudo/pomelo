package com.cuckoo.pomelo.common.dao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.cuckoo.pomelo.common.dao.datasource.DynamicDataSource;
import com.cuckoo.pomelo.common.dao.datasource.DynamicDataSourceManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源自动配置
 */
@Slf4j
@Configuration
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
public class CustomDataSourceAutoConfiguration {
    
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public DynamicDataSourceManager dynamicDataSourceManager() {
        DynamicDataSourceManager manager = DynamicDataSourceManager.getInstance();
        return manager;
    }
    
    @Bean
    @Primary
    public DataSource dataSource(DynamicDataSourceManager dataSourceManager) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        
        // 初始化时设置所有可用的数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        Map<String, DataSource> allDataSources = dataSourceManager.getAllDataSources();
        
        for (Map.Entry<String, DataSource> entry : allDataSources.entrySet()) {
            targetDataSources.put(entry.getKey(), entry.getValue());
        }
        
        // 设置默认数据源
        DataSource defaultDataSource = dataSourceManager.getCurrentDataSource();
        if (defaultDataSource == null) {
            log.warn("未找到当前数据源，将使用空数据源占位");
            defaultDataSource = new EmptyDataSource();
        }
        
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
        
        String currentName = dataSourceManager.getCurrentDataSourceName();
        log.info("动态数据源配置完成，默认数据源: {}, 共配置 {} 个数据源", currentName, targetDataSources.size());
        
        return dynamicDataSource;
    }
    
    /**
     * 空数据源，用于项目启动时没有配置数据源的情况
     */
    private static class EmptyDataSource implements DataSource {
        // 实现DataSource接口的所有方法，但抛出异常，提示用户需要配置数据源
        // 这里只实现getConnection方法作为示例，其他方法类似
        
        @Override
        public java.sql.Connection getConnection() throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public java.sql.Connection getConnection(String username, String password) throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public <T> T unwrap(Class<T> iface) throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public boolean isWrapperFor(Class<?> iface) throws java.sql.SQLException {
            return false;
        }
        
        @Override
        public java.io.PrintWriter getLogWriter() throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public void setLogWriter(java.io.PrintWriter out) throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public void setLoginTimeout(int seconds) throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public int getLoginTimeout() throws java.sql.SQLException {
            throw new java.sql.SQLException("未配置数据源，请先通过API配置数据源");
        }
        
        @Override
        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            throw new java.sql.SQLFeatureNotSupportedException("未配置数据源，请先通过API配置数据源");
        }
    }
}