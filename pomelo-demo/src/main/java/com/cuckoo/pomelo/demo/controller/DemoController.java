package com.cuckoo.pomelo.demo.controller;

import com.cuckoo.pomelo.demo.entity.User;
import com.cuckoo.pomelo.demo.service.UserService;
import com.cuckoo.pomelo.common.dao.service.DataSourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DataSourceManagementService dataSourceManagementService;
    
    @GetMapping("/public/hello")
    public String publicHello() {
        return "匿名访问成功";
    }

    @GetMapping("/user/info")
    @PreAuthorize("hasRole('USER')")
    public String userInfo() {
        return "用户信息访问成功";
    }

    @GetMapping("/admin/info")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminInfo() {
        return "管理员信息访问成功";
    }
    
    // ========== 数据库操作示例 ==========
    
    /**
     * 获取当前数据源信息
     */
    @GetMapping("/public/datasource/info")
    public Map<String, Object> getDataSourceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("currentDataSource", dataSourceManagementService.getCurrentDataSourceName());
        info.put("message", "当前使用的数据源");
        return info;
    }
    
    /**
     * 测试数据源连接（通过执行简单查询）
     */
    @GetMapping("/public/datasource/test")
    public Map<String, Object> testDataSourceConnection() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 尝试查询用户数据来测试数据源连接
            List<User> users = userService.findAllUsers();
            result.put("success", true);
            result.put("currentDataSource", dataSourceManagementService.getCurrentDataSourceName());
            result.put("userCount", users.size());
            result.put("message", "数据源连接正常");
        } catch (Exception e) {
            result.put("success", false);
            result.put("currentDataSource", dataSourceManagementService.getCurrentDataSourceName());
            result.put("error", e.getMessage());
            result.put("message", "数据源连接失败");
        }
        return result;
    }
    
    /**
     * 创建用户（演示数据库写操作）
     */
    @PostMapping("/public/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    /**
     * 查询所有用户（演示数据库读操作）
     */
    @GetMapping("/public/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
    
    /**
     * 根据ID查询用户
     */
    @GetMapping("/public/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/public/users/{id}")
    public boolean updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/public/users/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}