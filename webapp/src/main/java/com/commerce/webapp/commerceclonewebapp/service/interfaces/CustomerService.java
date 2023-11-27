package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import com.commerce.webapp.commerceclonewebapp.model.entity.Customer;

public interface CustomerService  {
    public Customer findByEmail(String email);
    public Customer register(Customer customer);

}
