package com.cuckoo.pomelo.common.security.dao.impl;

import com.cuckoo.pomelo.common.security.dao.UserRepository;
import com.cuckoo.pomelo.common.security.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    
    private final Map<String, User> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public InMemoryUserRepository() {
        // 初始化一些测试用户
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole("ADMIN");
        
        User normalUser = new User();
        normalUser.setId(2L);
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("user123"));
        normalUser.setRole("USER");
        
        users.put(adminUser.getUsername(), adminUser);
        users.put(normalUser.getUsername(), normalUser);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}