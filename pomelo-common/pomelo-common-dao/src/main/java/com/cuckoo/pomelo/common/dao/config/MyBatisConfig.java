package com.cuckoo.pomelo.common.dao.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.cuckoo.pomelo.**.mapper")
public class MyBatisConfig {
    // 可以在这里添加其他MyBatis相关配置
}