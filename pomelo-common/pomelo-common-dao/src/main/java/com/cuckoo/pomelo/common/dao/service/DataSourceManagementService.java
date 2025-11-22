package com.cuckoo.pomelo.common.dao.service;

import com.cuckoo.pomelo.common.dao.datasource.DynamicDataSourceManager;
import com.cuckoo.pomelo.common.dao.entity.DataSourceConfig;
import com.cuckoo.pomelo.common.dao.mapper.DataSourceConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源管理服务（避免循环依赖的独立服务）
 */
@Slf4j
@Service
public class DataSourceManagementService {
    
    private final DynamicDataSourceManager dataSourceManager = DynamicDataSourceManager.getInstance();
    
    @org.springframework.beans.factory.annotation.Autowired
    private com.cuckoo.pomelo.common.dao.mapper.DataSourceConfigMapper dataSourceConfigMapper;
    
    /**
     * 获取所有数据源配置
     */
    public List<DataSourceConfig> findAll() {
        try {
            List<DataSourceConfig> configs = dataSourceConfigMapper.findAll();
            return configs != null ? configs : new ArrayList<>();
        } catch (Exception e) {
            log.error("查询所有数据源配置失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据ID查询数据源配置
     */
    public DataSourceConfig findById(Long id) {
        try {
            return dataSourceConfigMapper.selectByPrimaryKey(id).orElse(null);
        } catch (Exception e) {
            log.error("根据ID查询数据源配置失败", e);
            return null;
        }
    }
    
    /**
     * 删除数据源配置
     */
    public boolean delete(Long id) {
        DataSourceConfig config = findById(id);
        if (config == null) {
            return false;
        }
        
        try {
            int deleted = dataSourceConfigMapper.deleteByPrimaryKey(id);
            if (deleted > 0) {
                // 同时从内存中移除数据源
                dataSourceManager.removeDataSource(config.getName());
                log.info("数据源配置 {} 已删除", config.getName());
                return true;
            }
        } catch (Exception e) {
            log.error("删除数据源配置失败", e);
        }
        
        return false;
    }
    
    /**
     * 添加数据源
     */
    public DataSourceConfig addDataSource(DataSourceConfig config) {
        return dataSourceManager.addDataSource(config);
    }
    
    /**
     * 更新数据源
     */
    public boolean updateDataSource(DataSourceConfig config) {
        return dataSourceManager.updateDataSource(config);
    }
    
    /**
     * 切换数据源
     */
    public boolean switchDataSource(String name) {
        return dataSourceManager.switchDataSource(name);
    }
    
    /**
     * 设置默认数据源
     */
    public boolean setDefaultDataSource(String name) {
        return dataSourceManager.setDefaultDataSource(name);
    }
    
    /**
     * 启用数据源
     */
    public boolean enableDataSource(String name) {
        return dataSourceManager.enableDataSource(name);
    }
    
    /**
     * 获取当前数据源名称
     */
    public String getCurrentDataSourceName() {
        return dataSourceManager.getCurrentDataSourceName();
    }
}