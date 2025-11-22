package com.cuckoo.pomelo.demo.service;

import com.cuckoo.pomelo.demo.entity.User;
import com.cuckoo.pomelo.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 创建用户
     */
    @Transactional
    public User createUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);
        return user;
    }
    
    /**
     * 根据ID查询用户
     */
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(id).orElse(null);
    }
    
    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    /**
     * 查询所有用户
     */
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }
    
    /**
     * 更新用户
     */
    @Transactional
    public boolean updateUser(User user) {
        user.setUpdateTime(new Date());
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public boolean deleteUser(Long id) {
        return userMapper.deleteByPrimaryKey(id) > 0;
    }
}