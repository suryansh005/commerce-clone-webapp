package com.commerce.webapp.commerceclonewebapp.config;


import com.commerce.webapp.commerceclonewebapp.security.filters.CustomAuthenticationFailureHandler;
import com.commerce.webapp.commerceclonewebapp.security.filters.CustomUsernamePasswordAuthenticationFilter;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;;
import com.commerce.webapp.commerceclonewebapp.security.filters.JwtAuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.commerce.webapp.commerceclonewebapp.security.filters.RefreshTokFilter;

@Configuration
@EnableWebSecurity
public class Security {
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private RefreshTokFilter refreshTokenFilter;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       AuthenticationManager authenticationManager =  authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/register","/login","/user/test")
                .permitAll()
                .anyRequest()
                    .authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login")
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilter(customUsernamePasswordAuthFilter(authenticationManager))
                .addFilterAfter(jwtAuthorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(refreshTokenFilter, JwtAuthorizationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter customUsernamePasswordAuthFilter(AuthenticationManager authenticationManager){
        CustomUsernamePasswordAuthenticationFilter authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager,jwtService,failureHandler);
        authenticationFilter.setFilterProcessesUrl("/auth");
        return authenticationFilter;
    }




}
