package com.cuckoo.pomelo.common.security.dao;

import com.cuckoo.pomelo.common.security.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}