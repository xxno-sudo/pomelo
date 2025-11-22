package com.cuckoo.pomelo.demo.entity;

import io.mybatis.provider.Entity;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类（示例）
 */
@Data
@Entity.Table("demo_user")
public class User {
    
    @Entity.Column(id = true, updatable = false)
    private Long id;
    
    @Entity.Column
    private String username;
    
    @Entity.Column
    private String email;
    
    @Entity.Column("create_time")
    private Date createTime;
    
    @Entity.Column("update_time")
    private Date updateTime;
}