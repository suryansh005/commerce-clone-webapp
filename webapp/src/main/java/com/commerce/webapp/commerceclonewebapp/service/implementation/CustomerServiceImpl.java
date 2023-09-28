package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.repository.CustomerRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer findByEmail(String email){
        return customerRepository.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User does not exit")
        );
    }
    public Customer register(Customer customer){
        return customerRepository.save(customer);
    }

}
