package com.commerce.webapp.commerceclonewebapp.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthRequest {
    private String username;
    private String password;
}
