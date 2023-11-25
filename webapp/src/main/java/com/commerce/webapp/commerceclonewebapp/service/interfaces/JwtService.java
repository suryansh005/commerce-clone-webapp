package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;

public interface JwtService {

    public String extractEmail(String jwtToken);

    String generateToken(Authentication authResult);

    String generateToken(Customer customer);

    boolean isTokenValid(Customer customer, String jwtToken);
}
