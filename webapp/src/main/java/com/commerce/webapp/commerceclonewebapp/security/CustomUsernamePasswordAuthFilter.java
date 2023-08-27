package com.commerce.webapp.commerceclonewebapp.security;

import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

//    @Autowired
    private  final AuthenticationManager authenticationManager;
//    @Autowired
    private  final JwtService jwtService;
    @Autowired
    public JwtUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,JwtService jwtService) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
           try{
               JwtAuthRequest jwtAuthRequest = new ObjectMapper().readValue(request.getInputStream(),JwtAuthRequest.class);
               UsernamePasswordAuthenticationToken authObject =  new UsernamePasswordAuthenticationToken(
                       jwtAuthRequest.getUsername(),
                       jwtAuthRequest.getPassword()
               );
               return authenticationManager.authenticate(authObject);
           }catch (Exception e){
                throw  new RuntimeException("Authentication Failed !!!");
           }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
            String jwtToken =  jwtService.generateToken(authResult);
            response.addHeader("Authorization" , "Bearer " + jwtToken);
    }

//    @Override
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    public void setJwtService(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }
}
