package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.http.OtpRequest;
import com.commerce.webapp.commerceclonewebapp.model.jms.JobMessage;
import com.commerce.webapp.commerceclonewebapp.model.jms.JobType;
import com.commerce.webapp.commerceclonewebapp.repository.OtpRequestRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.OTPService;
import com.commerce.webapp.commerceclonewebapp.util.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OTPServiceImpl implements OTPService {

    private final String otpServiceURL = "http://localhost:8081/sendotp";
    private static final String REGISTER_AMQ_TOPIC = "webapp-notification-Topic";

    @Autowired
    private JmsProducer jmsProducer;

    @Autowired
    private OtpRequestRepository otpRequestRepository;

    @Override
    @Async
    @Transactional
    public void sendOTP(String email, int retryAttempt) {
        if(retryAttempt <= 3) {
            try {
                RestTemplate template = new RestTemplate();

                sendAMQMessageToRegister(email);

            } catch (Exception ex) {
                System.out.println("Error occured while sending otp. Retrying.."+ex);
                sendOTP(email, ++retryAttempt);
            }
        }
    }

    @Async
    @Transactional
    private void sendAMQMessageToRegister(String email) throws Exception {
        JobMessage jobMessage = new JobMessage();
        jobMessage.setEmail(email);
        jobMessage.setJobType(JobType.SEND_OTP);
        jmsProducer.sendMessage(REGISTER_AMQ_TOPIC, jobMessage);
    }


    // VErification of OTP needs to be done using redis
    @Override
    public boolean verifyOtp(String email, String otpEntered){
        try {
            Optional<OtpRequest> otpRequestOptional = otpRequestRepository.findById(email);

            OtpRequest otpRequest = otpRequestOptional.isPresent() ? otpRequestOptional.get() : null;

            if (otpRequest != null && otpRequest.getOtpValue().equalsIgnoreCase(otpEntered)
                    && otpRequest.getRetryAttempt() < 3) {
                otpRequest.setRetryAttempt(otpRequest.getRetryAttempt() + 1);
                return true;
            }
        }catch(Exception ex){
            System.out.println("Exception occured "+ex);
        }

        return false;
    }
}
