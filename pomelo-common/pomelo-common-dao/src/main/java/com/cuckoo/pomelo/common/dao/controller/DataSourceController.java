package com.cuckoo.pomelo.common.dao.controller;

import com.cuckoo.pomelo.common.dao.entity.DataSourceConfig;
import com.cuckoo.pomelo.common.dao.service.DataSourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置API控制器
 */
@RestController
@RequestMapping("/api/datasource")
public class DataSourceController {
    
    @Autowired
    private DataSourceManagementService dataSourceManagementService;
    
    /**
     * 获取所有数据源配置
     */
    @GetMapping("/list")
    public List<DataSourceConfig> listDataSources() {
        return dataSourceManagementService.findAll();
    }
    
    /**
     * 获取当前数据源名称
     */
    @GetMapping("/current")
    public String getCurrentDataSource() {
        return dataSourceManagementService.getCurrentDataSourceName();
    }
    
    /**
     * 添加数据源
     */
    @PostMapping("/add")
    public DataSourceConfig addDataSource(@RequestBody DataSourceConfig config) {
        return dataSourceManagementService.addDataSource(config);
    }
    
    /**
     * 更新数据源
     */
    @PutMapping("/update")
    public boolean updateDataSource(@RequestBody DataSourceConfig config) {
        return dataSourceManagementService.updateDataSource(config);
    }
    
    /**
     * 删除数据源
     */
    @DeleteMapping("/delete/{id}")
    public boolean deleteDataSource(@PathVariable Long id) {
        return dataSourceManagementService.delete(id);
    }
    
    /**
     * 切换数据源
     */
    @PostMapping("/switch/{name}")
    public boolean switchDataSource(@PathVariable String name) {
        return dataSourceManagementService.switchDataSource(name);
    }
    
    /**
     * 设置默认数据源
     */
    @PostMapping("/default/{name}")
    public boolean setDefaultDataSource(@PathVariable String name) {
        return dataSourceManagementService.setDefaultDataSource(name);
    }
    
    /**
     * 启用数据源
     */
    @PostMapping("/enable/{name}")
    public boolean enableDataSource(@PathVariable String name) {
        return dataSourceManagementService.enableDataSource(name);
    }
    
    /**
     * 获取数据源状态信息
     */
    @GetMapping("/status")
    public Map<String, Object> getDataSourceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("currentDataSource", dataSourceManagementService.getCurrentDataSourceName());
        status.put("allDataSources", dataSourceManagementService.findAll());
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }
}