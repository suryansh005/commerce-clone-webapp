package com.commerce.webapp.commerceclonewebapp.security.filters;

import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper mapper ;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ReturnStatusParam.ReturnStatusParamBuilder builder =  ReturnStatusParam.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).success(false).statusCode(response.getStatus());
       if(exception instanceof UsernameNotFoundException){
           builder.message("User Does not exist");
       } else if (exception instanceof BadCredentialsException) {
           builder.message("Bad credentials");
       } else if (exception instanceof LockedException ) {
           builder.message("Account is locked");
       } else if (exception instanceof AccountExpiredException) {
           builder.message("User Account has expired");
       }else {
           builder.message("Some error occured");
       }

        // will add logger later
        System.out.println(exception);

        mapper.writeValue(response.getWriter(), builder.build());
    }
}
