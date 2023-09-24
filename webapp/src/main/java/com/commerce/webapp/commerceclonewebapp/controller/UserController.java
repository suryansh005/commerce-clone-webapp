package com.commerce.webapp.commerceclonewebapp.controller;


import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static com.commerce.webapp.commerceclonewebapp.util.Constants.REFRESH_TOKEN;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokService refreshTokService;

    @RequestMapping(value = "/register" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().body("heelo");
    }

    @RequestMapping(value = "/refresh-token")
    @ResponseBody
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response){
        Cookie refreshCookie = CookieUtil.getCookieByName(request,REFRESH_TOKEN);

        if(refreshCookie != null){
            String refreshTokenStr =  refreshCookie.getValue();
            try {

                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                Customer user =  customerService.findByEmail(userEmail);

//                RefreshToken refreshTokenDb = refreshTokService.findByToken(refreshTokenStr).orElseThrow(()->
//                        new RuntimeException(String.format("Token %s not found in DB",refreshTokenStr)));
//                //verify
//                refreshTokService.verifyToken(refreshTokenDb);
                //get user
//                Customer user =  refreshTokenDb.getCustomer();
                //generate jwt
                String newJwtToken =  jwtService.generateToken(user);
                response.addCookie(CookieUtil.generateJwtCookie(newJwtToken));

                return JsonUtil.genericSuccess();

            }catch (Exception e){
                CookieUtil.deleteCookie(response,request,REFRESH_TOKEN);
                System.out.println(e.getMessage());
                return JsonUtil.genericUnauthorized();
            }

        }
        System.out.println("refreshToken cookie absent");
        return JsonUtil.genericUnauthorized();
    }


}
