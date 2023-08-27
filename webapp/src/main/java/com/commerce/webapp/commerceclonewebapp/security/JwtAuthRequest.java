package com.commerce.webapp.commerceclonewebapp.security;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String username;
    private String password;
}
