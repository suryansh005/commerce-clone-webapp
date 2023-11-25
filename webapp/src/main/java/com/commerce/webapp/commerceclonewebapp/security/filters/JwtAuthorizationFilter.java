package com.commerce.webapp.commerceclonewebapp.security.filters;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;

import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import  java.util.List;

import static com.commerce.webapp.commerceclonewebapp.util.Constants.ACCESS_TOKEN;
import static com.commerce.webapp.commerceclonewebapp.util.Constants.BLANK;


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

    private static List<String> skipFilterUrls = Arrays.asList("/login","/user/register","/user/refresh-token","/user/producer/*", "/verifyOTP");


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipFilterUrls.stream().anyMatch( url -> new AntPathRequestMatcher(url).matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

            Cookie jwtCookie = CookieUtil.getCookieByName(request,ACCESS_TOKEN);
            String jwtToken = jwtCookie == null ? BLANK : jwtCookie.getValue();

            try {

                if(jwtToken.isEmpty()){
                /* throw 401 in this case also  as all apis need jwt verfication
                    is some  does not add them in skip urls
                */
//                filterChain.doFilter(request,response);
                    throw new BadCredentialsException("Empty jwt token");
//                return;
                }
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
                logger.error("Error occured During Jwt authorization " ,e );
                ReturnStatusParam ret = null;
                if(e instanceof ExpiredJwtException)
                    ret = JsonUtil.tokenErrResponse("Expired_Token_JWT");
                else
                    ret = JsonUtil.tokenErrResponse("Invalid_Token_JWT");

                mapper.writeValue(response.getWriter(), ret);

            }

    }
}
