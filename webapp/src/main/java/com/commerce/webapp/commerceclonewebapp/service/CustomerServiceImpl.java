package com.commerce.webapp.commerceclonewebapp.service;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.repository.CustomerRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer findByEmail(String email){
        return customerRepository.findByEmail(email);
    }
    public Customer register(Customer customer){
        return customerRepository.save(customer);
    }

}
