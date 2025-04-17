package ru.cinimex.taskservice.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String executeUserName(String token){
        return exctractClaim(token, Claims::getSubject);
    }

    public List<String> executeRole(String token){return exctractClaim(token,
            claims -> claims.get("roles", List.class));}


    private <T> T exctractClaim(String token, Function<Claims, T> claimsExtractor){
        final Claims claims = extractClaimsFromToken(token);
        return claimsExtractor.apply(claims);
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
