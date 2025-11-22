package com.cuckoo.pomelo.common.dao.constants;

/**
 * 数据源相关常量
 */
public final class DataSourceConstants {
    
    private DataSourceConstants() {
        // 工具类，禁止实例化
    }
    
    /**
     * 默认SQLite数据源名称
     */
    public static final String DEFAULT_SQLITE_DATASOURCE_NAME = "sqlite-default";
    
    /**
     * SQLite数据库文件路径
     */
    public static final String SQLITE_DB_FILE_PATH = "./pomelo-datasource.db";
    
    /**
     * SQLite JDBC URL
     */
    public static final String SQLITE_JDBC_URL = "jdbc:sqlite:" + SQLITE_DB_FILE_PATH;
    
    /**
     * SQLite驱动类名
     */
    public static final String SQLITE_DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    
    /**
     * 数据源状态：未启用
     */
    public static final int DATASOURCE_STATUS_DISABLED = 0;
    
    /**
     * 数据源状态：使用中
     */
    public static final int DATASOURCE_STATUS_ENABLED = 1;
    
    /**
     * 连接池配置
     */
    public static final class ConnectionPool {
        /**
         * SQLite连接池最小空闲连接数
         */
        public static final int SQLITE_MIN_IDLE = 1;
        
        /**
         * SQLite连接池最大连接数
         */
        public static final int SQLITE_MAX_POOL_SIZE = 5;
        
        /**
         * 其他数据源连接池最小空闲连接数
         */
        public static final int DEFAULT_MIN_IDLE = 5;
        
        /**
         * 其他数据源连接池最大连接数
         */
        public static final int DEFAULT_MAX_POOL_SIZE = 10;
        
        /**
         * 连接空闲超时时间（毫秒）
         */
        public static final long IDLE_TIMEOUT = 30000L;
        
        /**
         * 连接最大生命周期（毫秒）
         */
        public static final long MAX_LIFETIME = 1800000L;
        
        /**
         * 连接超时时间（毫秒）
         */
        public static final long CONNECTION_TIMEOUT = 30000L;
    }
    
    /**
     * SQL语句
     */
    public static final class SQL {
        /**
         * 创建数据源配置表
         */
        public static final String CREATE_DATA_SOURCE_CONFIG_TABLE = 
            "CREATE TABLE IF NOT EXISTS data_source_config (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL UNIQUE," +
            "url TEXT NOT NULL," +
            "username TEXT," +
            "password TEXT," +
            "driver_class_name TEXT NOT NULL," +
            "is_default INTEGER NOT NULL DEFAULT 0," +
            "status INTEGER NOT NULL DEFAULT 0," +
            "last_test_time TIMESTAMP," +
            "last_test_result INTEGER," +
            "last_test_message TEXT," +
            "create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            ");";
        
        /**
         * 创建示例用户表
         */
        public static final String CREATE_DEMO_USER_TABLE = 
            "CREATE TABLE IF NOT EXISTS demo_user (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL UNIQUE," +
            "email TEXT," +
            "create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            ");";
        
        /**
         * 插入示例用户数据
         */
        public static final String INSERT_SAMPLE_USERS = 
            "INSERT OR IGNORE INTO demo_user (username, email) VALUES " +
            "('admin', 'admin@example.com'), " +
            "('user1', 'user1@example.com'), " +
            "('user2', 'user2@example.com');";
    }
}