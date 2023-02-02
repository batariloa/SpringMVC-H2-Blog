package com.example.springmvch2blog.util;


import com.example.springmvch2blog.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;
    @Value("${jjwt.expirationTimeAccessToken}")
    private String expirationTimeAccessToken;
    @Value("${jjwt.expirationTimeRefreshToken}")
    private String expirationTimeRefreshToken;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {

        JwtParser parser = Jwts.parserBuilder()
                               .setSigningKey(key)
                               .build();
        Claims cl = parser.parseClaimsJws(token)
                          .getBody();
        return cl;
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);


        return expiration.before(new Date());
    }

    public String generateAccessToken(UserDto user) {

        Map<String, Object> claims = new HashMap<>();

        ;
        claims.put("role", user.getRole());

        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(
            Map<String, Object> claims,
            String username
    ) {

        Long expirationTimeLong = Long.parseLong(expirationTimeAccessToken);

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);


        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(createdDate)
                   .setExpiration(expirationDate)
                   .signWith(key)
                   .compact();
    }

    public String generateRefreshToken(UserDto user) {

        Map<String, Object> claims = new HashMap<>();


        claims.put("role", user.getRole());

        return doGenerateRefreshToken(claims, user.getUsername());
    }

    public String doGenerateRefreshToken(
            Map<String, Object> claims,
            String username
    ) {

        Long expirationTimeLong = Long.parseLong(expirationTimeRefreshToken);

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 90000);
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(createdDate)
                   .setExpiration(expirationDate)
                   .signWith(key)
                   .compact();
    }


    public Boolean validateToken(String token) {


        try {
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }


}
