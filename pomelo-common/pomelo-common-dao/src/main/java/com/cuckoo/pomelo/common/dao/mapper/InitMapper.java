package com.cuckoo.pomelo.common.dao.mapper;

import com.cuckoo.pomelo.common.dao.constants.DataSourceConstants;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * SQLite 初始化 Mapper：负责首次启动的建表与示例数据插入
 */
public interface InitMapper {
    /**
     * 创建数据源配置表
     */
    @Update(DataSourceConstants.SQL.CREATE_DATA_SOURCE_CONFIG_TABLE)
    void createDataSourceConfigTable();

    /**
     * 创建示例用户表
     */
    @Update(DataSourceConstants.SQL.CREATE_DEMO_USER_TABLE)
    void createDemoUserTable();

    /**
     * 插入示例数据（忽略已存在）
     */
    @Insert(DataSourceConstants.SQL.INSERT_SAMPLE_USERS)
    int insertSampleUsers();
}
