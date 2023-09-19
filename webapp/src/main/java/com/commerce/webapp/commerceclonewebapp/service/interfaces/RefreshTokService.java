package com.commerce.webapp.commerceclonewebapp.service.interfaces;

import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Authentication authResult);
     RefreshToken verifyToken(RefreshToken token);
}
