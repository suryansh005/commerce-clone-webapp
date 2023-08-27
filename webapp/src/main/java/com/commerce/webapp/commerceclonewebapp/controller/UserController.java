package com.commerce.webapp.commerceclonewebapp.controller;


import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody String customerJson){

        Customer uiCustomer = Customer.fromJsonToCustomer(customerJson);

        String email = uiCustomer.getEmail();

        if(email==null || email.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid email");
        }else {
            Customer dbCustomer = customerService.findByEmail(email);
            if(dbCustomer!=null)
                return ResponseEntity.ok("user already exists");
            Customer customer = Customer.builder()
                    .email(uiCustomer.getEmail())
                    .firstName(uiCustomer.getFirstName())
                    .lastName(uiCustomer.getLastName())
                    .password(passwordEncoder.encode(uiCustomer.getPassword()))
                    .isAccountNonExpired(true)
                    .isCredentialsNonExpired(true)
                    .isEnabled(true)
                    .isAccountNonLocked(true)
                    .contactNumber(uiCustomer.getContactNumber())
                    .countryCode(uiCustomer.getCountryCode())
                    .build();
            customerService.register(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body("user created");
        }

    }
    @RequestMapping(value = "/test")
    public ResponseEntity test(){

        return ResponseEntity.ok("hello");
    }
}
