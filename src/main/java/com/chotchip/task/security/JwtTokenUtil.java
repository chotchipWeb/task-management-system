package com.chotchip.task.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Setter
@Getter
public class JwtTokenUtil {

    private final JWTConfigurationProperties jwtConfigurationProperties;
    private String secretKey;
    private long expirationTime;

    @Autowired
    public JwtTokenUtil(JWTConfigurationProperties jwtConfigurationProperties) {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        setSecretKey(jwtConfigurationProperties.getSecretKey());
        setExpirationTime(Long.parseLong(jwtConfigurationProperties.getExpirationTime()));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
