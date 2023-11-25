package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.SecurityService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokService refreshTokService;

    @Override
    public void addJwtTokenAndRefreshTokenByCustomerEmail(String email, HttpServletResponse response) {

        Customer user =  customerService.findByEmail(email);

        String jwtToken =  jwtService.generateToken(user);
        response.addCookie(CookieUtil.generateJwtCookie(jwtToken));

        RefreshToken refreshToken = refreshTokService.createRefreshToken(email);
        response.addCookie(CookieUtil.generateRefreshCookie(refreshToken.getToken()));

        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    public void addJwtTokenAndRefreshTokenByAuthentication(Authentication auth, HttpServletResponse response) {

        addJwtTokenAndRefreshTokenByCustomerEmail(auth.getName(), response);

    }
}
