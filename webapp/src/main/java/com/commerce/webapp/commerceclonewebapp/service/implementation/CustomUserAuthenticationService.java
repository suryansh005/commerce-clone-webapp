package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.entity.Customer;
import com.commerce.webapp.commerceclonewebapp.model.SessionUser;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class CustomUserAuthenticationService implements UserDetailsService {

    @Autowired
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findByEmail(username);
        String email = customer.getEmail();
        String password = customer.getPassword();
        boolean enabled =  customer.isActive();
        boolean accountNonExpired =  !customer.isAccountExpired();
        boolean accountNonLocked =  !customer.isAccountLocked();
        boolean credentialNonExpired =  !customer.isAccountExpired();
        List<String> userRoles = new ArrayList<>();
        List<GrantedAuthority> authorityList = getAuthorities(userRoles);
        SessionUser user = new SessionUser(email , password , enabled , accountNonExpired , credentialNonExpired
        ,accountNonLocked ,authorityList
        );
        user.setCustomer(customer);
        return user;
    }

    private List<GrantedAuthority> getAuthorities(List<String>userRoles) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for(String role : userRoles){
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        return authorityList;
    }
}
