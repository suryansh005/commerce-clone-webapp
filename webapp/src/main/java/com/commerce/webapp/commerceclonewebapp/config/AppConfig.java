package com.commerce.webapp.commerceclonewebapp.config;


import com.commerce.webapp.commerceclonewebapp.security.filters.CustomAuthenticationFailureHandler;
import com.commerce.webapp.commerceclonewebapp.service.implementation.CustomUserAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
//    @Autowired
//    private CustomerRepository customerRepository;
    @Autowired
    private CustomUserAuthenticationService customUserAuthenticationService;

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//               return customerRepository.findByEmail(username).orElseThrow(
//                       () -> new UsernameNotFoundException("User does not exist")
//               );
//            }
//        };
//    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserAuthenticationService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
    @Bean
    public CustomAuthenticationFailureHandler failureHandler(){
        return new CustomAuthenticationFailureHandler();
    }





}
