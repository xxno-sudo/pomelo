package com.cuckoo.pomelo.common.dao.sqlite;

import com.cuckoo.pomelo.common.dao.constants.DataSourceConstants;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite数据库管理器
 */
@Slf4j
public class SQLiteManager {
    
    static {
        try {
            Class.forName(DataSourceConstants.SQLITE_DRIVER_CLASS_NAME);
            initDatabase();
        } catch (ClassNotFoundException e) {
            log.error("SQLite JDBC驱动加载失败", e);
            throw new RuntimeException("SQLite JDBC驱动加载失败", e);
        }
    }
    
    /**
     * 初始化SQLite数据库
     */
    private static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建数据源配置表
            stmt.execute(DataSourceConstants.SQL.CREATE_DATA_SOURCE_CONFIG_TABLE);
            
            // 创建示例用户表
            stmt.execute(DataSourceConstants.SQL.CREATE_DEMO_USER_TABLE);
            
            // 插入示例数据
            stmt.execute(DataSourceConstants.SQL.INSERT_SAMPLE_USERS);
            
            log.info("SQLite数据库初始化成功，已创建示例表和数据");
            
        } catch (SQLException e) {
            log.error("SQLite数据库初始化失败", e);
            throw new RuntimeException("SQLite数据库初始化失败", e);
        }
    }
    
    /**
     * 获取SQLite数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DataSourceConstants.SQLITE_JDBC_URL);
    }
}