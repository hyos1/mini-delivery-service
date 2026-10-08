package com.myapp.delivery.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_KEY = "auth";

    @Value(value = "${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 생성 (유저 ID, 권한(String), 만료시간)
    public String createToken(Long id, String userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(id))
                        .claim("userRole", userRole) // UserRole객체가 아닌 String 저장
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // Bearer 제거한 jwt값
    public String subStringToken(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            throw new IllegalArgumentException("토큰 값이 비어있습니다.");
        }
        if (!tokenValue.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("토큰 값은 Bearer 로 시작해야 합니다.");
        }
        return tokenValue.substring(7);
    }

    // 사용자 정보 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}