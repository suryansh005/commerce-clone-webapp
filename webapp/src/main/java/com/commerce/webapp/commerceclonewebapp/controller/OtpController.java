package com.commerce.webapp.commerceclonewebapp.controller;

import com.commerce.webapp.commerceclonewebapp.OtpVerificationDTO;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.OTPService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class OtpController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private SecurityService securityService;

    @PostMapping(value = "/verifyOTP")
    @ResponseBody
    public ResponseEntity<String> verifyOTP(@RequestBody OtpVerificationDTO otpVerificationRequest, HttpServletResponse response){
        try{
            Boolean isVerified = otpService.verifyOtp(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtpCode());
            if(isVerified){
                securityService.addJwtTokenAndRefreshTokenByCustomerEmail(otpVerificationRequest.getEmail(), response);
                return ResponseEntity.status(HttpStatus.OK).body("OTP verified");
            }
        } catch(Exception ex){
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP verification failed");
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Wrong OTP");
    }

}
