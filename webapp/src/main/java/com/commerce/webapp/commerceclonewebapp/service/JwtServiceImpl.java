package com.commerce.webapp.commerceclonewebapp.service;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {


    private static  final String SECRET_KEY = "OiKUkh+h8OWsEHzLIGNEXWARmPsx1Rtrv/oGELBnOZVhf+/cYgYVIjH3fdZWLj/MEr2AVp69J0UtZkq8EOHB3Ge0CZyz5MMeZghOUmITbB4=";
    @Override
    public String extractEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }
    public Claims extractAllClaims(String jwtToken){
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .parseClaimsJws(jwtToken)
                .getBody();
    }
    public <T> T extractClaim(String jwtToken , Function<Claims,T> claimResolver){
        return claimResolver.apply(extractAllClaims(jwtToken));
    }

    private Key getSigninKey() {
        byte [] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public String generateToken(Authentication authResult){

        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities",authResult.getAuthorities())
                .setIssuedAt(new Date())
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .compact();

    }
    public boolean isTokenValid(Customer customer, String jwtToken){
        return (isTokenNonExpired(jwtToken));
    }

    private boolean isTokenNonExpired(String jwtToken) {
        return extractClaim(jwtToken,Claims::getExpiration).before(new java.sql.Date(System.currentTimeMillis()));
    }

}
