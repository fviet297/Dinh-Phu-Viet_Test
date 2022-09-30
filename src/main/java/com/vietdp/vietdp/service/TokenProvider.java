package com.vietdp.vietdp.service;

import com.vietdp.vietdp.dao.TokenRepository;
import com.vietdp.vietdp.dao.UserRepository;
import com.vietdp.vietdp.entity.Tokens;
import com.vietdp.vietdp.entity.Users;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Environment environment;
    public final String JWT_SECRET_DEV = Base64.getEncoder().encodeToString("123456".getBytes());
    private final long JWT_EXPIRATION_DEV = 60 * 60 * 1000L;
    private final long REFRESH_TOKEN = 30 * 24 * 60 * 60 * 1000L;
    public String getSecret() {
        return JWT_SECRET_DEV;
    }
    public long getExpiration() {
        return JWT_EXPIRATION_DEV;
    }

    public long getExpirationRefreshToken() {
        return REFRESH_TOKEN;
    }

    // Tạo ra jwt từ thông tin user
    public String generateToken(AuthUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()
                + getExpiration()
        );
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecret())
                .compact();
    }

    public String generateRefreshToken(AuthUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()
                + getExpirationRefreshToken()
        );
        String tokens = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSecret())
                .compact();
        Users users = userRepository.findByEmail(userDetails.getUsername()).get();
        Tokens token = new Tokens();
        token.setUserId(users.getId());
        token.setExpiresIn(expiryDate.toString());
        token.setRefreshToken(tokens);
        tokenRepository.save(token);

        return tokens;
    }


    // Lấy thông tin user từ jwt
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public String getUsernameFromJWTUnisign(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
    // Lấy thông tin user từ jwt
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_DEV)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }



}

