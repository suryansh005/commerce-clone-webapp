package com.commerce.webapp.commerceclonewebapp.repository;

import com.commerce.webapp.commerceclonewebapp.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Long> {
    public Optional<RefreshToken>findByToken(String token);

}
