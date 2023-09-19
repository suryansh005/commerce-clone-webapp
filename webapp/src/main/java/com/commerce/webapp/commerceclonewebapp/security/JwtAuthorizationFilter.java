package com.commerce.webapp.commerceclonewebapp.security;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_STR = "Authorization" ;
    private static final String BEARER_STR =  "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private  ObjectMapper mapper ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /* request header method
        String authHeader = request.getHeader(AUTHORIZATION_STR);
        if(authHeader == null || !authHeader.startsWith(BEARER_STR)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwtToken = authHeader.substring(7);

        */
        if(request.getServletPath().equals("/login") || request.getServletPath().equals("/auth")){
            filterChain.doFilter(request,response);
        }
        else {

            Cookie[] cookies = request.getCookies();
            String jwtToken = "";
            if(cookies!=null){
                for (Cookie cookie : cookies){
                    if(cookie.getName().equals("accessToken")){
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
            if(jwtToken.isEmpty()){
                filterChain.doFilter(request,response);
                return;
            }
            try {
                String customerEmail = jwtService.extractEmail(jwtToken);
                if (customerEmail != null && !customerEmail.isEmpty()) {
                    Customer customer = customerService.findByEmail(customerEmail);
                    if (customer != null && jwtService.isTokenValid(customer, jwtToken)) {
                        UsernamePasswordAuthenticationToken authObj = new UsernamePasswordAuthenticationToken(
                                customer, null, customer.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authObj);
                        authObj.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    }
                }
                filterChain.doFilter(request,response);
            }catch (Exception e){

                ReturnStatusParam.ReturnStatusParamBuilder builder =  ReturnStatusParam.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).success(false);
                if(e instanceof ExpiredJwtException)
                    builder.message("Access Token Expired");
                else
                    builder.message("Invalid Token");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), builder.build());

            }
        }

    }
}
