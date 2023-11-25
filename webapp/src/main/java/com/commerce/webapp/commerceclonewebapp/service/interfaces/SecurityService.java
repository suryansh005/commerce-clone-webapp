package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;

public interface SecurityService {

    void addJwtTokenAndRefreshTokenByCustomerEmail(String email, HttpServletResponse response);

    void addJwtTokenAndRefreshTokenByAuthentication(Authentication auth, HttpServletResponse response);

}
