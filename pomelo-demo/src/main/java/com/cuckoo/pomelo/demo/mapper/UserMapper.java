package com.cuckoo.pomelo.demo.mapper;

import com.cuckoo.pomelo.demo.entity.User;
import io.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
public interface UserMapper extends Mapper<User, Long> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM demo_user WHERE username = #{username}")
    User findByUsername(String username);
    
    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM demo_user ORDER BY create_time DESC")
    List<User> findAllUsers();
}