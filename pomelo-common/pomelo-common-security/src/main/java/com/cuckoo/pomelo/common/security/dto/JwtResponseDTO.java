package com.cuckoo.pomelo.common.security.dto;

import lombok.Getter;

@Getter
public class JwtResponseDTO {
    private final String token;
    private final long expiresIn;

    public JwtResponseDTO(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}