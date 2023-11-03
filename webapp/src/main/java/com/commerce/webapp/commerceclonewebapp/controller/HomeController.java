package com.commerce.webapp.commerceclonewebapp.controller;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller

public class HomeController {

    @Autowired
    JwtService jwtService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpServletRequest request){
        System.out.println("inside login page");
        try {
            Cookie [] cookies = request.getCookies();

            if(cookies != null){
                String jwtToken = "";
                for(Cookie _c: cookies){
                    if(_c.getName().equals("jwtToken")){
                        jwtToken = _c.getValue();break;
                    }
                }

                if(!jwtToken.isEmpty()){
                    String customerEmail = jwtService.extractEmail(jwtToken);
                    Customer customer = customerService.findByEmail(customerEmail);
                    if(customer==null || !jwtService.isTokenValid(customer,jwtToken)){
//                    return "login";
                        throw new RuntimeException();
                    }

                return "redirect:/user/test";
//                    return JsonUtil.genericLoginRedirect();
                }
            }
        }catch (Exception e){
            System.out.println("Error " +  e.getMessage());
//            return JsonUtil.genericLoginError();
            return "login";

        }
        return "login";
//        return JsonUtil.genericLoginSuccess();
    }
}
