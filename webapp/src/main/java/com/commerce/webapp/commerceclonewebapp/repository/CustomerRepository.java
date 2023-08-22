package com.commerce.webapp.commerceclonewebapp.repository;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    public Customer findByEmail(String email);

}
