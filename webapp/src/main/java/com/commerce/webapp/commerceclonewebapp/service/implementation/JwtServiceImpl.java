package com.commerce.webapp.commerceclonewebapp.service.implementation;

import com.commerce.webapp.commerceclonewebapp.model.Customer;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.security.Key;
import java.time.Instant;

import java.util.Date;
import java.util.function.Function;

import static com.commerce.webapp.commerceclonewebapp.util.Constants.AUTHORITIES_STR;


@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret.key}")
    private  String SECRET_KEY;

    @Override
    public String extractEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken , Function<Claims,T> claimResolver){
        return claimResolver.apply(extractAllClaims(jwtToken));
    }

    public Claims extractAllClaims(String jwtToken){
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigninKey() {
        byte [] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(Authentication authResult){

        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities",authResult.getAuthorities())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date(Instant.now().plusSeconds(86400).toEpochMilli()))
                .compact();

    }

    public String generateToken(Customer customer){

        return Jwts.builder()
                .setSubject(customer.getUsername())
                .claim(AUTHORITIES_STR,customer.getAuthorities())
                .setIssuedAt(new Date())
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date(Instant.now().plusSeconds(86400).toEpochMilli()))
                .compact();

    }

    public boolean isTokenValid(Customer customer, String jwtToken){
        return (isTokenNonExpired(jwtToken));
    }

    private boolean isTokenNonExpired(String jwtToken) {
        return extractClaim(jwtToken,Claims::getExpiration).after(new Date(Instant.now().toEpochMilli()));
    }
}
