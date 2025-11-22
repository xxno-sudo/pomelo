package com.cuckoo.pomelo.common.dao.config;

import com.cuckoo.pomelo.common.dao.datasource.DynamicDataSource;
import com.cuckoo.pomelo.common.dao.datasource.DynamicDataSourceManager;
import com.cuckoo.pomelo.common.dao.mapper.InitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 数据源初始化器
 * 在应用启动完成后执行数据源的延迟初始化
 */
@Slf4j
@Component
public class DataSourceInitializer implements ApplicationRunner {
    
    @Autowired
    private InitMapper initMapper;
    
    @Autowired
    private DynamicDataSourceManager dataSourceManager;
    
    @Autowired
    private DynamicDataSource dynamicDataSource;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始执行MyBatis建表初始化...");
        
        // 通过 MyBatis 执行 DDL 和示例数据插入
        try {
            initMapper.createDataSourceConfigTable();
            initMapper.createDemoUserTable();
            try {
                initMapper.insertSampleUsers();
            } catch (Exception ignore) {
                // 插入示例数据失败不影响启动（可能已存在）
                log.debug("示例数据插入忽略: {}", ignore.getMessage());
            }
            log.info("MyBatis 建表与示例数据初始化完成");
        } catch (Exception e) {
            log.error("MyBatis 初始化数据库失败", e);
            throw e;
        }
        
        // 可选等待，确保其他 Bean 初始化后再刷新路由
        Thread.sleep(500);
        
        // 刷新动态数据源配置
        dataSourceManager.refreshDynamicDataSource();
        
        log.info("数据源延迟初始化完成，当前数据源: {}", dataSourceManager.getCurrentDataSourceName());
    }
}