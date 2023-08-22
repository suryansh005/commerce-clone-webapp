package com.commerce.webapp.commerceclonewebapp.service;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer findByEmail(String email){
        return customerRepository.findByEmail(email);
    }
    public Customer register(Customer customer){
        return customerRepository.save(customer);
    }
}
