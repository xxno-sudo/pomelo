package com.cuckoo.pomelo.common.dao.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数据源配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity.Table("data_source_config")
public class DataSourceConfig extends BaseEntity {
    /**
     * 配置ID
     */
    @Entity.Column(id = true, updatable = false)
    private Long id;
    
    /**
     * 数据源名称
     */
    @Entity.Column
    private String name;
    
    /**
     * 数据源URL
     */
    @Entity.Column
    private String url;
    
    /**
     * 数据库用户名
     */
    @Entity.Column
    private String username;
    
    /**
     * 数据库密码
     */
    @Entity.Column
    private String password;
    
    /**
     * 数据库驱动类名
     */
    @Entity.Column("driver_class_name")
    private String driverClassName;
    
    /**
     * 是否为默认数据源
     */
    @Entity.Column("is_default")
    private Boolean isDefault;
    
    /**
     * 数据源状态（0-未启用，1-使用中）
     */
    @Entity.Column
    private Integer status;
    
    /**
     * 最后一次连接测试时间
     */
    @Entity.Column("last_test_time")
    private Date lastTestTime;
    
    /**
     * 最后一次连接测试结果（true-成功，false-失败）
     */
    @Entity.Column("last_test_result")
    private Boolean lastTestResult;
    
    /**
     * 连接测试失败原因
     */
    @Entity.Column("last_test_message")
    private String lastTestMessage;
}