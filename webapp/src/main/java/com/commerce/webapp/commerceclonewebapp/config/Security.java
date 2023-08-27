package com.commerce.webapp.commerceclonewebapp.config;


import com.commerce.webapp.commerceclonewebapp.security.JwtUsernamePasswordAuthFilter;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;;
import  com.commerce.webapp.commerceclonewebapp.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class Security {
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtFilter jwtFilter;

//    @Autowired
//    JwtUsernamePasswordAuthFilter jwtUsernamePasswordAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       AuthenticationManager authenticationManager =  authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilter(jwtUsernamePasswordAuthFilter(authenticationManager))
                .addFilterAfter(jwtFilter,JwtUsernamePasswordAuthFilter.class);

        return http.build();
    }


 @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtUsernamePasswordAuthFilter jwtUsernamePasswordAuthFilter(AuthenticationManager authenticationManager){
        return new JwtUsernamePasswordAuthFilter(authenticationManager,jwtService);
    }




}
