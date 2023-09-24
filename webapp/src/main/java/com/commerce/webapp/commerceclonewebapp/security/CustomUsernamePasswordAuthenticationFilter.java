package com.commerce.webapp.commerceclonewebapp.security;

import com.commerce.webapp.commerceclonewebapp.model.DummyAuthentication;
import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import com.commerce.webapp.commerceclonewebapp.repository.CustomerRepository;
import com.commerce.webapp.commerceclonewebapp.repository.RefreshTokenRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.UUID;


public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private  final AuthenticationManager authenticationManager;

    private  final JwtService jwtService;

    protected final DummyAuthentication dummy = new DummyAuthentication(null);
    @Autowired
    private  ObjectMapper mapper ;

    @Autowired
    private RefreshTokService refreshTokService;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
           try{
//               JwtAuthRequest jwtAuthRequest = new ObjectMapper().convertValue(request.getParameterMap(),JwtAuthRequest.class);
               JwtAuthRequest jwtAuthRequest = JwtAuthRequest.builder().
                                                username(request.getParameter("username"))
                                                .password(request.getParameter("password")).build();

               UsernamePasswordAuthenticationToken authObject =  new UsernamePasswordAuthenticationToken(
                       jwtAuthRequest.getUsername(),
                       jwtAuthRequest.getPassword()
               );
               return authenticationManager.authenticate(authObject);
           }catch (Exception e){
                logger.error(e.getMessage());
                return dummy;
           }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        ReturnStatusParam.ReturnStatusParamBuilder builder =  ReturnStatusParam.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).success(true);
        try {
            if(authResult.isAuthenticated() && !authResult.getName().isEmpty()){

                String jwtToken =  jwtService.generateToken(authResult);

                RefreshToken refreshToken = refreshTokService.createRefreshToken(authResult);

                response.addCookie(CookieUtil.generateJwtCookie(jwtToken));
                response.addCookie(CookieUtil.generateRefreshCookie(refreshToken.getToken()));
                response.setStatus(HttpStatus.OK.value());

                builder.message("Authenticated").statusCode(response.getStatus());

            }else{
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                builder.message("Authentication Error").success(false).statusCode(response.getStatus());
            }

        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            builder.message("Authentication Error").success(false).statusCode(response.getStatus());
        }
        response.setContentType("application/json; charset=utf-8");
        mapper.writeValue(response.getWriter(), builder.build());
    }


}
