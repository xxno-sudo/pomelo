package com.cuckoo.pomelo.common.dao.datasource;

import com.cuckoo.pomelo.common.dao.constants.DataSourceConstants;
import com.cuckoo.pomelo.common.dao.entity.DataSourceConfig;


import com.cuckoo.pomelo.common.dao.mapper.DataSourceConfigMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源管理器
 */
@Slf4j
public class DynamicDataSourceManager implements ApplicationContextAware {
    private static final DynamicDataSourceManager INSTANCE = new DynamicDataSourceManager();
    
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    /**
     * -- GETTER --
     *  获取当前数据源名称
     */
    @Getter
    private String currentDataSourceName;
    
    private ApplicationContext applicationContext;
    private volatile boolean initialized = false;
    
    private DynamicDataSourceManager() {
        // 私有构造函数
    }
    
    public static DynamicDataSourceManager getInstance() {
        return INSTANCE;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    /**
     * 初始化数据源
     */
    public void init() {
        if (initialized) {
            return;
        }
        
        try {
            // 首先初始化SQLite本地数据源作为默认数据源
            initDefaultSQLiteDataSource();
            
            // 延迟加载其他数据源配置（避免循环依赖）
            loadDataSourceConfigsLater();
            
            initialized = true;
            log.info("数据源管理器初始化完成，当前数据源: {}", currentDataSourceName);
        } catch (Exception e) {
            log.error("初始化数据源失败", e);
        }
    }
    
    /**
     * 延迟加载数据源配置
     */
    private void loadDataSourceConfigsLater() {
        // 使用新线程延迟加载，避免循环依赖
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 等待Spring容器完全启动
                loadDataSourceConfigs();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("延迟加载数据源配置被中断");
            }
        }).start();
    }
    
    /**
     * 加载数据源配置
     */
    private void loadDataSourceConfigs() {
        try {
            // 直接从SQLite读取配置，避免依赖MyBatis
            List<DataSourceConfig> configs = loadDataSourceConfigsFromSQLite();
            
            for (DataSourceConfig config : configs) {
                createAndRegisterDataSource(config);
                
                // 如果是默认数据源，则切换
                if (config.getIsDefault()) {
                    currentDataSourceName = config.getName();
                    log.info("已设置默认数据源: {}", currentDataSourceName);
                }
            }
            
            log.info("延迟加载数据源配置完成，共加载 {} 个数据源", configs.size());
        } catch (Exception e) {
            log.error("延迟加载数据源配置失败", e);
        }
    }
    
    /**
     * 直接从SQLite读取数据源配置
     */
    private List<DataSourceConfig> loadDataSourceConfigsFromSQLite() {
        List<DataSourceConfig> configs = new ArrayList<>();
        
        try {
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                List<DataSourceConfig> all = mapper.findAll();
                for (DataSourceConfig c : all) {
                    if (c.getStatus() != null && c.getStatus() == 1) {
                        configs.add(c);
                    }
                }
            }
        } catch (Exception e) {
            log.error("从SQLite读取数据源配置失败", e);
        }
        
        return configs;
    }
    
    /**
     * 初始化默认SQLite数据源
     */
    private void initDefaultSQLiteDataSource() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(DataSourceConstants.SQLITE_JDBC_URL);
            hikariConfig.setDriverClassName(DataSourceConstants.SQLITE_DRIVER_CLASS_NAME);
            hikariConfig.setPoolName("HikariPool-SQLite-Default");
            
            // SQLite特殊配置
            hikariConfig.setMinimumIdle(DataSourceConstants.ConnectionPool.SQLITE_MIN_IDLE);
            hikariConfig.setMaximumPoolSize(DataSourceConstants.ConnectionPool.SQLITE_MAX_POOL_SIZE);
            hikariConfig.setIdleTimeout(DataSourceConstants.ConnectionPool.IDLE_TIMEOUT);
            hikariConfig.setMaxLifetime(DataSourceConstants.ConnectionPool.MAX_LIFETIME);
            hikariConfig.setConnectionTimeout(DataSourceConstants.ConnectionPool.CONNECTION_TIMEOUT);
            hikariConfig.setAutoCommit(true);
            
            DataSource dataSource = new HikariDataSource(hikariConfig);
            dataSourceMap.put(DataSourceConstants.DEFAULT_SQLITE_DATASOURCE_NAME, dataSource);
            currentDataSourceName = DataSourceConstants.DEFAULT_SQLITE_DATASOURCE_NAME;
            
            log.info("默认SQLite数据源初始化成功");
        } catch (Exception e) {
            log.error("初始化默认SQLite数据源失败", e);
            throw new RuntimeException("初始化默认SQLite数据源失败", e);
        }
    }
    
    /**
     * 创建并注册数据源
     */
    private void createAndRegisterDataSource(DataSourceConfig config) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getUrl());
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.setDriverClassName(config.getDriverClassName());
            hikariConfig.setPoolName("HikariPool-" + config.getName());
            
            // 设置连接池参数
            hikariConfig.setMinimumIdle(DataSourceConstants.ConnectionPool.DEFAULT_MIN_IDLE);
            hikariConfig.setMaximumPoolSize(DataSourceConstants.ConnectionPool.DEFAULT_MAX_POOL_SIZE);
            hikariConfig.setIdleTimeout(DataSourceConstants.ConnectionPool.IDLE_TIMEOUT);
            hikariConfig.setMaxLifetime(DataSourceConstants.ConnectionPool.MAX_LIFETIME);
            hikariConfig.setConnectionTimeout(DataSourceConstants.ConnectionPool.CONNECTION_TIMEOUT);
            hikariConfig.setAutoCommit(true);
            
            DataSource dataSource = new HikariDataSource(hikariConfig);
            dataSourceMap.put(config.getName(), dataSource);
            log.info("数据源 {} 创建成功", config.getName());
            
            if (config.getIsDefault()) {
                currentDataSourceName = config.getName();
            }
        } catch (Exception e) {
            log.error("创建数据源 {} 失败", config.getName(), e);
        }
    }
    
    /**
     * 验证数据源连接
     */
    /**
     * 验证数据源连接
     */
    private DataSourceValidationResult validateDataSource(DataSourceConfig config) {
        DataSourceValidationResult result = new DataSourceValidationResult();
        result.setSuccess(false);
        
        try {
            // 加载驱动
            Class.forName(config.getDriverClassName());
            
            // 尝试建立连接
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    config.getUrl(), 
                    config.getUsername(), 
                    config.getPassword())) {
                
                // 测试连接是否有效
                if (conn.isValid(5)) { // 5秒超时
                    result.setSuccess(true);
                    result.setMessage("连接成功");
                } else {
                    result.setMessage("连接无效");
                }
            }
        } catch (Exception e) {
            result.setMessage("连接失败：" + e.getMessage());
            log.error("数据源连接验证失败", e);
        }
        
        return result;
    }
    
    /**
     * 添加新数据源
     */
    public DataSourceConfig addDataSource(DataSourceConfig config) {
        // 先进行数据源验证
        DataSourceValidationResult validationResult = validateDataSource(config);
        
        // 更新验证结果
        config.setLastTestTime(new java.util.Date());
        config.setLastTestResult(validationResult.isSuccess());
        config.setLastTestMessage(validationResult.getMessage());
        
        // 如果验证失败，直接返回，不保存到数据库
        if (!validationResult.isSuccess()) {
            return config;
        }
        
        // 验证成功，保存配置到数据库
        config.setStatus(DataSourceConstants.DATASOURCE_STATUS_DISABLED); // 默认未启用
        DataSourceConfig savedConfig = saveDataSourceConfigToSQLite(config);
        
        // 创建并注册数据源
        createAndRegisterDataSource(savedConfig);
        
        // 如果是默认数据源，则切换到该数据源
        if (savedConfig.getIsDefault()) {
            currentDataSourceName = savedConfig.getName();
        }
        
        // 刷新动态数据源配置
        refreshDynamicDataSource();
        
        return savedConfig;
    }
    
    /**
     * 直接保存数据源配置到SQLite
     */
    private DataSourceConfig saveDataSourceConfigToSQLite(DataSourceConfig config) {
        try {
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                Date now = new Date();
                config.setCreateTime(now);
                config.setUpdateTime(now);
                mapper.insert(config);
                log.info("数据源配置 {} 已保存到SQLite", config.getName());
            }
        } catch (Exception e) {
            log.error("保存数据源配置到SQLite失败", e);
            throw new RuntimeException("保存数据源配置失败", e);
        }
        
        return config;
    }
    

    
    /**
     * 启用数据源
     */
    public boolean enableDataSource(String name) {
        DataSourceConfig config = findDataSourceConfigByName(name);
        if (config == null) {
            log.warn("数据源 {} 不存在，无法启用", name);
            return false;
        }
        
        // 验证数据源
        DataSourceValidationResult validationResult = validateDataSource(config);
        if (!validationResult.isSuccess()) {
            // 更新验证结果
            config.setLastTestTime(new java.util.Date());
            config.setLastTestResult(false);
            config.setLastTestMessage(validationResult.getMessage());
            updateDataSourceConfigInSQLite(config);
            
            log.warn("数据源 {} 验证失败，无法启用: {}", name, validationResult.getMessage());
            return false;
        }
        
        // 更新状态为使用中
        config.setStatus(DataSourceConstants.DATASOURCE_STATUS_ENABLED);
        config.setLastTestTime(new java.util.Date());
        config.setLastTestResult(true);
        config.setLastTestMessage(validationResult.getMessage());
        
        boolean updated = updateDataSourceConfigInSQLite(config);
        if (updated) {
            // 刷新动态数据源配置
            refreshDynamicDataSource();
            return switchDataSource(name);
        }
        
        return false;
    }
    
    /**
     * 根据名称查找数据源配置
     */
    private DataSourceConfig findDataSourceConfigByName(String name) {
        try {
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                return mapper.findByName(name);
            }
        } catch (Exception e) {
            log.error("从SQLite查询数据源配置失败", e);
        }
        
        return null;
    }
    
    /**
     * 更新数据源配置到SQLite
     */
    private boolean updateDataSourceConfigInSQLite(DataSourceConfig config) {
        try {
            config.setUpdateTime(new Date());
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                int updated = mapper.updateByPrimaryKeySelective(config);
                return updated > 0;
            }
        } catch (Exception e) {
            log.error("更新数据源配置到SQLite失败", e);
        }
        return false;
    }
    
    /**
     * 更新数据源
     */
    public boolean updateDataSource(DataSourceConfig config) {
        boolean updated = updateDataSourceConfigInSQLite(config);
        
        if (updated) {
            // 移除旧数据源
            removeDataSource(config.getName());
            
            // 创建并注册新数据源
            createAndRegisterDataSource(config);
            
            // 如果是默认数据源，则切换到该数据源
            if (config.getIsDefault()) {
                currentDataSourceName = config.getName();
            }
            
            // 刷新动态数据源配置
            refreshDynamicDataSource();
        }
        
        return updated;
    }
    
    /**
     * 移除数据源
     */
    public boolean removeDataSource(String name) {
        DataSource dataSource = dataSourceMap.remove(name);
        
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            log.info("数据源 {} 已关闭并移除", name);
            
            // 如果移除的是当前数据源，则切换到默认数据源或第一个可用数据源
            if (name.equals(currentDataSourceName)) {
                DataSourceConfig defaultConfig = findDefaultDataSourceConfig();
                if (defaultConfig != null) {
                    currentDataSourceName = defaultConfig.getName();
                } else if (!dataSourceMap.isEmpty()) {
                    currentDataSourceName = dataSourceMap.keySet().iterator().next();
                } else {
                    currentDataSourceName = DataSourceConstants.DEFAULT_SQLITE_DATASOURCE_NAME; // 回退到默认SQLite
                }
                
                log.info("当前数据源已切换为: {}", currentDataSourceName);
            }
            
            // 刷新动态数据源配置
            refreshDynamicDataSource();
            
            return true;
        }
        
        return false;
    }
    
    /**
     * 切换数据源
     */
    public boolean switchDataSource(String name) {
        if (dataSourceMap.containsKey(name)) {
            String oldDataSource = currentDataSourceName;
            currentDataSourceName = name;
            
            log.info("已从数据源 {} 切换到数据源: {}", oldDataSource, name);
            
            // 刷新动态数据源配置
            refreshDynamicDataSource();
            
            return true;
        }
        
        log.warn("数据源 {} 不存在，无法切换", name);
        return false;
    }
    

    
    /**
     * 设置默认数据源
     */
    public boolean setDefaultDataSource(String name) {
        DataSourceConfig config = findDataSourceConfigByName(name);
        
        if (config != null) {
            boolean success = setDefaultDataSourceInSQLite(config.getId());
            
            if (success) {
                currentDataSourceName = name;
                log.info("已将 {} 设置为默认数据源", name);
                
                // 刷新动态数据源配置
                refreshDynamicDataSource();
            }
            
            return success;
        }
        
        log.warn("数据源 {} 不存在，无法设置为默认数据源", name);
        return false;
    }
    
    /**
     * 在SQLite中设置默认数据源
     */
    private boolean setDefaultDataSourceInSQLite(Long id) {
        try {
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                int updated = mapper.setDefault(id);
                return updated > 0;
            }
        } catch (Exception e) {
            log.error("设置默认数据源失败", e);
        }
        return false;
    }
    
    /**
     * 查找默认数据源配置
     */
    private DataSourceConfig findDefaultDataSourceConfig() {
        try {
            DataSourceConfigMapper mapper = getMapper();
            if (mapper != null) {
                return mapper.findDefault();
            }
        } catch (Exception e) {
            log.error("查找默认数据源配置失败", e);
        }
        
        return null;
    }
    
    private DataSourceConfigMapper getMapper() {
        try {
            return this.applicationContext.getBean(DataSourceConfigMapper.class);
        } catch (Exception e) {
            log.warn("获取 DataSourceConfigMapper 失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取当前数据源
     */
    public DataSource getCurrentDataSource() {
        if (currentDataSourceName != null) {
            return dataSourceMap.get(currentDataSourceName);
        }
        
        return null;
    }
    
    /**
     * 获取指定名称的数据源
     */
    public DataSource getDataSource(String name) {
        return dataSourceMap.get(name);
    }

    /**
     * 获取所有数据源
     */
    public Map<String, DataSource> getAllDataSources() {
        return new ConcurrentHashMap<>(dataSourceMap);
    }
    
    /**
     * 刷新动态数据源配置
     */
    public void refreshDynamicDataSource() {
        if (applicationContext != null) {
            try {
                DynamicDataSource dynamicDataSource = applicationContext.getBean(DynamicDataSource.class);
                dynamicDataSource.refreshDataSources();
                log.info("动态数据源配置已刷新");
            } catch (Exception e) {
                log.warn("刷新动态数据源配置失败: {}", e.getMessage());
            }
        }
    }
}