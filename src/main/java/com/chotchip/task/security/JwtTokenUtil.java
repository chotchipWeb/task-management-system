package com.chotchip.task.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    private final static String SECRET_KEY = "94E6D2B2F2B34E9AD2E8706A35814E39D859135BEBBEBCF5FD91AFEB0D1330593FD000DF2AE81D9705E9358DD3C11A95CAC9A1389ACE02BD0EEB86B10FABD681";
    private final static  long EXPIRATION_TIME = 3600000L;
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private static Date extractExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
