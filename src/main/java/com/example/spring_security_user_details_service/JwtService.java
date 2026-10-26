package com.example.spring_security_user_details_service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

//    @Value("${jwt.refresh.token.expiration}")
//    private String refreshTokenExpiration;



    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Authorities se role strings ki list nikaali (e.g., ["ROLE_USER", "ROLE_ADMIN"])
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        return generateTokenFromUsernameWithClaims(claims,userDetails.getUsername());
    }

    public String generateTokenFromUsernameWithClaims(Map<String, Object> claims ,String username) {

        return Jwts.builder()
                .header()
                    .type("JWT") // ya custom type jaise "Bearer"

                .and()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(SignatureAlgorithm.HS256, secret)
                .claims(claims)
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getJwtFromHeader(String token) {
        return "";
    }

    public boolean validateToken(String token) {
        return true;
    }

}
