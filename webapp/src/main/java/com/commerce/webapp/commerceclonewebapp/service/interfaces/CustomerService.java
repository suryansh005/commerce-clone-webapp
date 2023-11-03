package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService  {
    public Customer findByEmail(String email);
    public Customer register(Customer customer);

}
