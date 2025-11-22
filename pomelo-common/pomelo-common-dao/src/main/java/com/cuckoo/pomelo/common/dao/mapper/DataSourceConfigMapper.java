package com.cuckoo.pomelo.common.dao.mapper;

import com.cuckoo.pomelo.common.dao.entity.DataSourceConfig;
import io.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 数据源配置Mapper
 */
public interface DataSourceConfigMapper extends Mapper<DataSourceConfig, Long> {
    
    /**
     * 查询默认数据源配置
     */
    @Select("SELECT * FROM data_source_config WHERE is_default = 1")
    DataSourceConfig findDefault();
    
    /**
     * 根据名称查询数据源配置
     */
    @Select("SELECT * FROM data_source_config WHERE name = #{name}")
    DataSourceConfig findByName(@Param("name") String name);
    
    /**
     * 设置默认数据源
     */
    @Select("UPDATE data_source_config SET is_default = CASE WHEN id = #{id} THEN 1 ELSE 0 END")
    int setDefault(@Param("id") Long id);
    
    /**
     * 查询所有数据源配置
     */
    @Select("SELECT * FROM data_source_config ORDER BY create_time DESC")
    java.util.List<DataSourceConfig> findAll();
}