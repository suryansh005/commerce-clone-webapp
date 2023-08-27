package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import org.springframework.security.core.Authentication;

public interface JwtService {
    public String extractEmail(String jwtToken);

    String generateToken(Authentication authResult);

    boolean isTokenValid(Customer customer, String jwtToken);
}
