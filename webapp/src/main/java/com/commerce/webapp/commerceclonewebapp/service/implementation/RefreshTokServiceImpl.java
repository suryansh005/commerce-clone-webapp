package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.repository.CustomerRepository;
import com.commerce.webapp.commerceclonewebapp.repository.RefreshTokenRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokServiceImpl implements RefreshTokService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Authentication authResult){

        return createRefreshToken(authResult.getName());

    }

    public RefreshToken createRefreshToken(String email){

        RefreshToken refreshToken = new RefreshToken();

        Customer tokenUser =  customerService.findByEmail(email);

        refreshToken.setCustomer(tokenUser);
        refreshToken.setExpiryDate(new Date(Instant.now().plus(30 , ChronoUnit.DAYS ).toEpochMilli()));
        refreshToken.setToken(UUID.randomUUID().toString());

        RefreshToken refreshTokenDb = refreshTokenRepository.save(refreshToken);

        return refreshTokenDb;
    }

    public RefreshToken verifyToken(RefreshToken token) {
        if (token.getExpiryDate().toInstant().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Expired_Token_JWT");
        }
        return token;
    }

    public Long deleteByUserId(Long userId){
       return refreshTokenRepository.deleteByUser(
               customerRepository.findById(userId).get()
       );
    }
}
