package com.commerce.webapp.commerceclonewebapp.controller;


import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.OTPService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static com.commerce.webapp.commerceclonewebapp.util.Constants.REFRESH_TOKEN;

@RestController
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

    @Autowired
    private OTPService otpService;

    @RequestMapping(value = "/register" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody String customerJson){

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred");

        try{
            Customer uiCustomer = Customer.fromJsonToCustomer(customerJson);
            response = register(uiCustomer);

            if(response.getStatusCode() == HttpStatus.CREATED){
                otpService.sendOTP(uiCustomer.getEmail(), 1);
            }
        } catch (Exception ex){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occured");
            System.out.println("Error occured");
        }
        return response;

    }

    private ResponseEntity<String> register(Customer uiCustomer){
        String email = uiCustomer.getEmail();

        if(email==null || email.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid email");
        }else {
            try {
                Customer dbCustomer = customerService.findByEmail(email);
                if(dbCustomer!=null)
                    return ResponseEntity.ok("user already exists");
            }catch (UsernameNotFoundException e){
                /* left empty becuase customerService.findByEmail(email)
                    will throw UsernameNotFoundException when user not found in
                    Db
                * */

            }

            Customer customer = Customer.builder()
                    .email(uiCustomer.getEmail())
                    .name(uiCustomer.getName())
                    .password(passwordEncoder.encode(uiCustomer.getPassword()))
                    .isAccountExpired(false)
                    .isCredentialsExpired(false)
                    .isActive(true)
                    .isAccountLocked(false)
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
            ////All verification already done in RefreshToknFilter
                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                Customer user =  customerService.findByEmail(userEmail);
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
