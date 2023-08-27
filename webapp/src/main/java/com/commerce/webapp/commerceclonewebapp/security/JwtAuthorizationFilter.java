package com.commerce.webapp.commerceclonewebapp.security;

import com.commerce.webapp.commerceclonewebapp.config.Security;
import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_STR = "Authorization" ;
    private static final String BEARER_STR =  "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerService customerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_STR);
        if(authHeader == null || !authHeader.startsWith(BEARER_STR)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwtToken = authHeader.substring(7);

        String customerEmail = jwtService.extractEmail(jwtToken);
        if(customerEmail != null && !customerEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication()!=null){
            Customer customer = customerService.findByEmail(customerEmail);
            if(jwtService.isTokenValid(customer,jwtToken)){
                UsernamePasswordAuthenticationToken authObj =  new UsernamePasswordAuthenticationToken(
                    customer, null, customer.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authObj);
                authObj.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        }
        filterChain.doFilter(request,response);

    }
}
